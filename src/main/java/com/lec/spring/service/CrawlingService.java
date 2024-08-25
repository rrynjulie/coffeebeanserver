package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.Product;
import com.lec.spring.domain.User;
import com.lec.spring.domain.enums.DealingStatus;
import com.lec.spring.domain.enums.Status;
import com.lec.spring.repository.AttachmentRepository;
import com.lec.spring.repository.ProductRepository;
import com.lec.spring.repository.UserRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Integer.parseInt;

@Service
public class CrawlingService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private UserRepository userRepository;

    private static final int MAX_PAGE = 1; // 최대 페이지
    private static final String BASE_URL = "https://web.joongna.com";

    private static final int[] CATEGORY_IDS = {
            111,
            112, 139, 140,
            143, 144, 180, 181,
            182, 185, 208, 209,
            210, 211, 224, 225
    };

    private static final int MAX_RETRIES = 3; // 최대 재시도 횟수
    private static final int TIMEOUT = 5000; // 타임아웃 시간 (밀리초)

    private static final String STATE_FILE = "crawl_state.txt";

    private final Random random = new Random();

    public void saveAllProducts() {
        int startCategoryIndex = 0;
        int startPage = 1;

        // 크롤링 상태 파일이 있으면 로드
        File stateFile = new File(STATE_FILE);
        if (stateFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(stateFile))) {
                startCategoryIndex = Integer.parseInt(reader.readLine());
                startPage = Integer.parseInt(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = startCategoryIndex; i < CATEGORY_IDS.length; i++) {
            int categoryId = CATEGORY_IDS[i];
            String baseUrl = "https://web.joongna.com/search?category=" + categoryId + "&page=";

            for (int page = (i == startCategoryIndex ? startPage : 1); page <= MAX_PAGE; page++) {
                String url = baseUrl + page;
                boolean success = saveProductDetailsWithRetries(url, MAX_RETRIES);

                if (success) {
                    // 크롤링 상태 저장
                    saveCrawlState(i, page + 1);
                } else {
                    // 실패한 경우 중단
                    return;
                }
            }
        }

        // 크롤링이 완료되면 상태 파일 삭제
        if (stateFile.exists()) {
            stateFile.delete();
        }
    }

    private void saveCrawlState(int categoryIndex, int nextPage) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STATE_FILE))) {
            writer.write(String.valueOf(categoryIndex));
            writer.newLine();
            writer.write(String.valueOf(nextPage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean saveProductDetailsWithRetries(String url, int retries) {
        while (retries > 0) {
            try {
                saveProductDetails(url);
                return true; // 성공하면 true 반환
            } catch (SocketException e) {
                retries--;
                if (retries == 0) {
                    System.err.println("최대 재시도 횟수를 초과했습니다. URL: " + url);
                } else {
                    System.err.println("연결 재설정 예외 발생, 재시도합니다. 남은 재시도 횟수: " + retries);
                    try {
                        Thread.sleep(2000); // 재시도 전 대기 시간
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false; // 다른 IOException 발생 시 false 반환
            }
        }
        return false;
    }

    public void saveProductDetails(String url) throws IOException {
        try {
            Document doc = Jsoup.connect(url)
                    .timeout(TIMEOUT)
                    .get();
            Elements links = doc.select("a.relative.group.box-border"); // 링크가 담긴 HTML 요소 선택자

            for (Element productElement : links) {
                String productLink = productElement.attr("href");
                String productUrl = BASE_URL + productLink;

                // 딜레이
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                Document productDoc = Jsoup.connect(productUrl)
                        .timeout(TIMEOUT)
                        .get();

                // JSON 부분
                Elements scriptTags = productDoc.select("script#__NEXT_DATA__");
                if (scriptTags.isEmpty()) {
                    System.out.println("No <script> tag with id '__NEXT_DATA__' found.");
                    continue;
                }

                String jsonText = scriptTags.first().html();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(jsonText);

                // JSON 데이터의 경로를 따라 원하는 데이터를 추출합니다.
                JsonNode productMediaNode = rootNode.path("props").path("pageProps").path("dehydratedState")
                        .path("queries").get(0).path("state").path("data")
                        .path("data").path("productMedia");

                if (productMediaNode.isMissingNode()) {
                    System.out.println("productMedia node is missing.");
                    continue;
                }

                // 상품 이름
                String name = productDoc.select("div.flex.items-center.justify-between.mb-1 h1").text();

                // 상품 설명
                String description = productDoc.select("div[name=product-description] article > p").text();

                // 가격
                String priceText = productDoc.select("div.flex.items-center.mb-2 > div.font-bold").text().replaceAll("[^\\d]", "");
                int price = 0;
                if (!priceText.isEmpty()) {
                    try {
                        price = Integer.parseInt(priceText);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        price = 0; // 기본값 설정
                    }
                }

                // 필수 필드가 비어있다면 다음 링크로 넘어가기
                if (name == null || name.isEmpty() || price == 0) {
                    System.out.println("삭제되거나 없는 게시글  " + productUrl);
                    continue;
                }

                // 카테고리 1, 2, 3
                Elements categories = productDoc.select("div.flex.items-center.w-full.chawkbazarBreadcrumb ol li a");
                String category1 = categories.size() > 1 ? categories.get(1).text() : "";
                String category2 = categories.size() > 2 ? categories.get(2).text() : "";
                String category3 = categories.size() > 3 ? categories.get(3).text() : "";

                // 상태
                String status = productDoc.select("ul.box-border.flex.text-center.border li:nth-child(1) button").text();

                // 거래 방식
                String dealingType = productDoc.select("ul.box-border.flex.text-center.border li:nth-child(2) button").text();

                // 거래 희망 지역
                String area = productDoc.select("div[name=product-description] button.inline-flex.gap-1.text-xs.text-jnblack.items-center").text();

                // 저장
                Product product = new Product();

                long randomUserId = random.nextInt(50) + 1; // 매번 호출할 때마다 랜덤 값 생성
                User user = userRepository.findById(randomUserId)
                        .orElseThrow(() -> new RuntimeException("User with ID " + randomUserId + " not found"));
                product.setUser(user);

                product.setName(name);
                product.setDescription(description);
                product.setPrice(price);
                product.setRegDate(DateUtil.getRandomDateWithinLastWeek()); // 랜덤 날짜 설정
                product.setDealingStatus(DealingStatus.valueOf("판매중"));
                product.setCategory1(category1);
                product.setCategory2(category2);
                product.setCategory3(category3);
                product.setStatus(Status.valueOf(status));
                product.setDealingType(dealingType);
                product.setDesiredArea(area);
                product.setViewCount(10);

                productRepository.save(product);

                // Attachment 저장
                Iterator<JsonNode> mediaIterator = productMediaNode.elements();
                while (mediaIterator.hasNext()) {
                    JsonNode mediaNode = mediaIterator.next();
                    String mediaUrl = mediaNode.path("mediaUrl").asText();
                    String productMediaSeq = mediaNode.path("productMediaSeq").asText();

                    if (!mediaUrl.isEmpty() && !productMediaSeq.isEmpty()) {
                        Attachment attachment = new Attachment();

                        attachment.setProduct(product);
                        attachment.setFilename(productMediaSeq);
                        attachment.setSource(mediaUrl);

                        attachmentRepository.save(attachment);

                        System.out.println("Attachment 저장 " + attachment);
                    }
                }

                System.out.println(product);
            }
        }catch (IOException e){
            System.out.println("저장실패");
            throw e;
        }
    }

    // 새로운 DateUtil 클래스 추가
    public static class DateUtil {

        public static LocalDateTime getRandomDateWithinLastWeek() {
            // 현재 시간
            LocalDateTime now = LocalDateTime.now();

            // 7일 전의 시간
            LocalDateTime weekAgo = now.minusDays(7);

            // weekAgo부터 now까지의 난수 생성
            long randomSeconds = ThreadLocalRandom.current().nextLong(ChronoUnit.SECONDS.between(weekAgo, now));

            // 생성된 난수를 기반으로 랜덤 시간 생성
            return weekAgo.plusSeconds(randomSeconds);
        }
    }
}
