package com.lec.spring.service;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.Car;
import com.lec.spring.domain.User;
import com.lec.spring.domain.enums.DealingStatus;
import com.lec.spring.domain.enums.Status;
import com.lec.spring.repository.AttachmentRepository;
import com.lec.spring.repository.CarRepository;
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
import java.util.Random;

@Service
public class CrawlingCarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String BASE_URL = "https://www.bobaedream.co.kr";
    private static final String STATE_FILE = "crawl_state.txt";
    private static final int MAX_RETRIES = 3; // 최대 재시도 횟수
    private static final int TIMEOUT = 10000; // 타임아웃 시간 (밀리초)
    private static final int MAX_PAGE = 1; // 최대 페이지 (필요에 따라 조정)
    private static final int VIEW_SIZE = 70; // 페이지 당 보기 사이즈

    private final Random random = new Random();

    private static final String[] GUBUN = {"K", "I"}; //국산차, 외제차
    private static final int[][] MAKER_IDS = {
            {
//                49, 1010, 3,
                    8, 26, 31}, // 현대, 제네시스, 기아, 쉐보레/대우, 르노코리아(삼성), KG모빌리티(쌍용)

            {21, 43, 1, 41, 16, 22, 11, 1006, 12} // 벤츠, 포르쉐, BMW, 페라리, 롤스로이스, 벤틀리, 람보르기니, 테슬라, 랜드로버
    };
    private static final String[][] MAKER_NAMES = {
            {
//                "현대", "제네시스", "기아",
                    "쉐보레", "르노코리아(삼성)", "KG모빌리티(쌍용)"},
            {"벤츠", "포르쉐", "BMW", "페라리", "롤스로이스", "벤틀리", "람보르기니", "테슬라", "랜드로버"}
    };

    public void saveAllProducts() {
        int startGubunIndex = 0;
        int startMakerIndex = 0;
        int startPage = 1;

//        // 크롤링 상태 파일이 있으면 로드
//        File stateFile = new File(STATE_FILE);
//        if (stateFile.exists()) {
//            try (BufferedReader reader = new BufferedReader(new FileReader(stateFile))) {
//                startGubunIndex = Integer.parseInt(reader.readLine());
//                startMakerIndex = Integer.parseInt(reader.readLine());
//                startPage = Integer.parseInt(reader.readLine());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        for (int gubunIndex = startGubunIndex; gubunIndex < GUBUN.length; gubunIndex++) {
            for (int makerIndex = (gubunIndex == startGubunIndex ? startMakerIndex : 0); makerIndex < MAKER_IDS[gubunIndex].length; makerIndex++) {
                int makerId = MAKER_IDS[gubunIndex][makerIndex];
                String baseUrl = BASE_URL + "/mycar/mycar_list.php?gubun=" + GUBUN[gubunIndex] + "&maker_no=" + makerId + "&order=S11&view_size=" + VIEW_SIZE + "&page=";

                String category1 = GUBUN[gubunIndex].equals("K") ? "국산차" : "외제차";
                String category2 = MAKER_NAMES[gubunIndex][makerIndex];

                for (int page = (makerIndex == startMakerIndex ? startPage : 1); page <= MAX_PAGE; page++) {
                    String url = baseUrl + page;
                    boolean success = saveProductDetailsWithRetries(url, MAX_RETRIES, category1, category2);

                    // 에러가 발생해도 크롤링을 계속 진행
                    if (success) {
                        // 크롤링 상태 저장
                        saveCrawlState(gubunIndex, makerIndex, page + 1);
                    } else {
                        System.err.println("URL 크롤링 실패: " + url);
                        // 실패한 경우에도 다음 페이지로 진행
                        saveCrawlState(gubunIndex, makerIndex, page + 1);
                    }
                }
            }
        }

//        // 크롤링이 완료되면 상태 파일 삭제
//        if (stateFile.exists()) {
//            stateFile.delete();
//        }
    }

    private void saveCrawlState(int gubunIndex, int makerIndex, int nextPage) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STATE_FILE))) {
            writer.write(String.valueOf(gubunIndex));
            writer.newLine();
            writer.write(String.valueOf(makerIndex));
            writer.newLine();
            writer.write(String.valueOf(nextPage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean saveProductDetailsWithRetries(String url, int retries, String category1, String category2) {
        while (retries > 0) {
            try {
                saveProductDetails(url, category1, category2);
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
                System.err.println("IO 예외 발생: " + e.getMessage());
                return false; // 다른 IOException 발생 시 false 반환
            }
        }
        return false;
    }

    private void saveProductDetails(String url, String category1, String category2) throws IOException {
        try {
            Document doc = Jsoup.connect(url).timeout(TIMEOUT).get();
            Elements productItems = doc.select(".product-item");
            for (Element productElement : productItems) {

                Car car = new Car();

                String detailsUrl = BASE_URL + productElement.select(".title .tit a").attr("href");
                System.out.println("url: " + detailsUrl);
                // 딜레이
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                Document carDoc = Jsoup.connect(detailsUrl)
                        .timeout(TIMEOUT)
                        .get();

                // 차량 이름 추출
                Element nameElement = carDoc.select("div.group-info .title-area h3.tit").first();
                String name = nameElement != null ? nameElement.text() : "";

                // 차량 가격 추출
                Element priceElement = carDoc.select("div.group-info .price-area .price b.cr").first();
                String priceText = priceElement != null ? priceElement.text().replaceAll("[^0-9]", "") : "";
                int price = !priceText.isEmpty() ? Integer.parseInt(priceText) : 0;

                // 차량번호 추출
                Element carNumElement = carDoc.select("div.gallery-data dl dd:contains(차량번호)").first();
                String carNum = carNumElement != null ? carNumElement.text().replace("차량번호 ", "") : "";

                // 유효성 검증
                if (name.isEmpty() || carNum.isEmpty()) {
                    System.out.println("정보가 부족하여 건너뜁니다. 이름: " + name + ", 가격: " + price + ", 차량번호: " + carNum);
                    continue;
                }

//            System.out.println("이름: " + name);
//            System.out.println("가격: " + price + "만원");
//            System.out.println("차량번호: " + carNum);

                String introduce = carDoc.select("div.detail-explanation > div.explanation-box").text();
//            System.out.println(detailExplanation);

                // 연식 추출
                Element modelYearElement = carDoc.select("tbody tr:contains(연식) td").first();
                Integer modelYear = modelYearElement != null ? Integer.parseInt(modelYearElement.text().substring(0, 4)) : null;

                // 주행거리 추출
                Element distanceElement = carDoc.select("tbody tr:contains(주행거리) td").first();
                Double distance = distanceElement != null ? Double.parseDouble(distanceElement.text().replaceAll("[^0-9]", "")) : null;

                //배기량
                Element displacementElement = carDoc.select("tbody tr:contains(배기량) td").get(1);
                Integer displacement = null;

                if (displacementElement != null) {
                    String displacementText = displacementElement.text();
                    int ccIndex = displacementText.indexOf("cc");
                    if (ccIndex != -1) {
                        String numberPart = displacementText.substring(0, ccIndex).replaceAll("[^0-9]", "");
                        if (!numberPart.isEmpty()) {
                            displacement = Integer.parseInt(numberPart);
                            car.setDisplacement(displacement);
                        }
                    }
                } else {
                    car.setDisplacement(0);
                }


                // 연료 추출
                Element fuelElement = carDoc.select("tbody tr:contains(연료) td").first();
                String fuel = fuelElement != null ? fuelElement.text() : "";

                // 변속기 추출
                Element transmissionElement = carDoc.select("tbody tr:contains(변속기) td").first();
                String transmission = transmissionElement != null ? transmissionElement.text() : "";

//            System.out.println("연식: " + modelYear);
//            System.out.println("주행거리: " + distance + " km");
//            System.out.println("배기량: " + displacement + " cc");
//            System.out.println("연료: " + fuel);
//            System.out.println("변속기: " + transmission);

                // 차량번호/소유자변경 정보 추출
                Element ownerChangeElement = carDoc.select("th:containsOwn(차량번호/소유자변경) + td").first();
                if (ownerChangeElement != null) {
                    String ownerChangeText = ownerChangeElement.text();
                    String[] ownerChangeParts = ownerChangeText.split("/");
                    Integer ownerChange = Integer.parseInt(ownerChangeParts[1].replaceAll("[^0-9]", "").trim());
//                System.out.println("소유자 변경: " + ownerChange);
                    car.setOwnerChange(ownerChange); // 소유자 변경 이력 횟수
                } else {
                    car.setOwnerChange(0);
                }


                // 보험사고(내차피해) 정보 추출
                Element insuranceVictimElement = carDoc.select("th:containsOwn(보험사고\\(내차피해\\)) + td").first();
                if (insuranceVictimElement != null) {
                    String insuranceVictimText = insuranceVictimElement.text();
                    Integer insuranceVictim = Integer.parseInt(insuranceVictimText.split("회")[0].trim());
//                System.out.println("보험사고(내차피해): " + insuranceVictim);
                    car.setInsuranceVictim(insuranceVictim); //보험사고(가해) 이력 횟수
                } else {
                    car.setInsuranceVictim(0);
                }


                // 보험사고(타차가해) 정보 추출
                Element insuranceInjurerElement = carDoc.select("th:containsOwn(보험사고\\(타차가해\\)) + td").first();
                if (insuranceInjurerElement != null) {
                    String insuranceInjurerText = insuranceInjurerElement.text();
                    Integer insuranceInjurer = Integer.parseInt(insuranceInjurerText.split("회")[0].trim());
//                System.out.println("보험사고(타차가해): " + insuranceInjurer);
                    car.setInsuranceInjurer(insuranceInjurer); // 보험사고(피해) 이력횟수
                } else {
                    car.setInsuranceInjurer(0);
                }

//            // category1, category2 출력
//            System.out.println("category1: " + category1);
//            System.out.println("category2: " + category2);


                long randomUserId = random.nextInt(50) + 1; // 매번 호출할 때마다 랜덤 값 생성
                User user = userRepository.findById(randomUserId)
                        .orElseThrow(() -> new RuntimeException("User with ID " + randomUserId + " not found"));
                car.setUser(user);

                car.setName(name); //중고차 이름
                car.setPrice(price); //중고차 가격
                car.setIntroduce(introduce); //중고차 소개
                car.setRegDate(LocalDateTime.now());
                car.setCategory1(category1); //카테고리1
                car.setCategory2(category2); //카테고리2
                car.setModelYear(modelYear); //연식
                car.setCarNum(carNum); //차량번호
                car.setDistance(distance); //주행거리
                car.setFuel(fuel); //연료
                car.setTransmission(transmission); //변속기
                car.setDealingStatus(DealingStatus.판매중);
                car.setStatus(Status.중고);
                car.setViewCount(10);

                carRepository.save(car);

                System.out.println(car);

                // 이미지 URL 추출 및 저장
                Elements imgElements = carDoc.select("#imgPos li a");
                for (Element imgElement : imgElements) {
                    String imgUrl = imgElement.attr("href");
                    String imgName = imgElement.attr("alt");

                    // URL이 상대 경로일 경우 절대 경로로 변경
                    if (imgUrl.startsWith("//")) {
                        imgUrl = "https:" + imgUrl;
                    }

                    // URL이 유효한 경우만 처리합니다.
                    if (!imgUrl.isEmpty()) {
                        Attachment attachment = new Attachment();
                        attachment.setCar(car);
                        attachment.setSource(imgUrl);
                        attachment.setFilename(imgName);
                        attachmentRepository.save(attachment);
                    }
                }

            }
        }catch (IOException e){
            System.out.println("저장실패");
            throw e;
        }
    }
}
