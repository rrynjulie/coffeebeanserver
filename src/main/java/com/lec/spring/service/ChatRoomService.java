package com.lec.spring.service;

import com.lec.spring.domain.*;
import com.lec.spring.domain.enums.DealingStatus;
import com.lec.spring.repository.ChatRoomRepository;
import com.lec.spring.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // 새로운 채팅방 생성 및 데이터베이스 저장
    @Transactional
    public ChatRoom createChatRoom(Long productId, Long buyerUserId) {
        Product product = productService.readOne(productId);
        User buyer = userService.readOne(buyerUserId);
        User seller = product.getUser();

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setProduct(product);
        chatRoom.setBuyerId(buyer); // 올바르게 설정됨
        chatRoom.setSellerId(seller); // 올바르게 설정됨
        chatRoom.setIsJoin(2L);  // isJoin 을 2로 설정
        chatRoom.setDealComplete(false); // 거래 완료 여부 초기값 설정

        chatRoom = chatRoomRepository.save(chatRoom);

        // 초기 메세지 생성
        String productName = chatRoom.getProduct().getName();
        String initialMessageText = String.format("안녕하세요! [%s] 보고 문의드립니다.", productName);

        Message initialMessage = new Message();
        initialMessage.setMessageText(initialMessageText);
        initialMessage.setSendTime(LocalDateTime.now(ZoneId.of("Asia/Seoul"))); // 한국 시간으로 설정
        initialMessage.setIsRead(false);
        initialMessage.setChatRoom(chatRoom);
        initialMessage.setSender(buyer);

        // 메세지 저장
        messageRepository.save(initialMessage);

        return chatRoom;
    }
    public ChatRoom findByBuyerAndSeller(Long chatRoomId) {
        return chatRoomRepository.findByBuyerAndSeller(chatRoomId)
                .map(chatRoom -> {
                    // Lazy loading 을 보장하기 위해 엔티티의 User 정보를 강제로 접근.
                    chatRoom.getBuyerId();
                    chatRoom.getSellerId();
                    return chatRoom;
                })
                .orElseThrow(() -> new RuntimeException("Chat room not found."));
    }

    // 사용자가 참여하고 있는 모든 채팅방 조회
    public List<ChatRoom> findByUserId(Long userId) {
        return chatRoomRepository.findByUserId(userId);
    }

    // ChatRoom ID로 조회
    public Optional<ChatRoom> findById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId);
    }

    // 채팅방 목록과 각 채팅방의 마지막 메시지를 가져오는 메소드
    @Transactional
    public List<ChatRoom> findChatRoomsWithLastMessage(Long userId) {
        List<ChatRoom> chatRooms = findByUserId(userId);

        for (ChatRoom chatRoom : chatRooms) {
            List<Message> lastMessages = messageRepository.findLastMessageByChatRoomId(
                    chatRoom.getChatRoomId(), PageRequest.of(0, 1));
            if (!lastMessages.isEmpty()) {
                Message lastMessage = lastMessages.get(0);
                chatRoom.setLastMessage(lastMessage.getMessageText());
                chatRoom.setLastSendTime(lastMessage.getSendTime().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime()); // 한국 시간 설정
            } else {
                chatRoom.setLastMessage("대화 내용이 없습니다.");
                chatRoom.setLastSendTime(null);
            }

            Long unreadMessageCount = messageRepository.unreadMessage(chatRoom.getChatRoomId(), userId);
            chatRoom.setUnreadMessage(unreadMessageCount != null ? unreadMessageCount : 0L);

            Product product = chatRoom.getProduct();
            if (product != null) {
                List<Attachment> attachments = product.getFileList();
                chatRoom.setAttachments(attachments); // ChatRoom 에 첨부파일 리스트 추가
            }
        }

        // 마지막 메시지의 전송 시간을 기준으로 채팅방 목록 정렬
        chatRooms.sort((cr1, cr2) -> {
            LocalDateTime time1 = cr1.getLastSendTime();
            LocalDateTime time2 = cr2.getLastSendTime();
            if (time1 == null) return 1;  // 시간이 없는 항목을 뒤로 보냄
            if (time2 == null) return -1; // 시간이 없는 항목을 뒤로 보냄
            return time2.compareTo(time1); // 내림차순 정렬
        });
        return chatRooms;
    }

    // 채팅방 목록에서 각 채팅방의 상품 정보만 가져오는 메소드
    @Transactional
    public Product getProductByChatRoomId(Long chatRoomId) {
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findById(chatRoomId);
        if (chatRoomOpt.isPresent()) {
            ChatRoom chatRoom = chatRoomOpt.get();
            return chatRoom.getProduct();
        }
        return null; // 또는 적절한 예외를 던질 수 있습니다.
    }

    // 채팅방 나가기
    public void leaveChatRoom(Long chatRoomId, Long userId) {
        try {
            Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(chatRoomId);
            if (optionalChatRoom.isPresent()) {
                ChatRoom chatRoom = optionalChatRoom.get();

                if (chatRoom.getSellerId() != null && chatRoom.getSellerId().equals(userId)) {
                    chatRoom.setSellerId(null);
                } else if (chatRoom.getBuyerId() != null && chatRoom.getBuyerId().equals(userId)) {
                    chatRoom.setBuyerId(null);
                }

                updateChatRoomJoin(chatRoom);
                chatRoomRepository.save(chatRoom);
            } else {
                throw new RuntimeException("Chat room not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error leaving chat room: " + e.getMessage());
        }
    }

    // 채팅방 참여 상태 업데이트
    private void updateChatRoomJoin(ChatRoom chatRoom) {
        Long buyerId = chatRoom.getBuyerId() != null ? chatRoom.getBuyerId() : null;
        Long sellerId = chatRoom.getSellerId() != null ? chatRoom.getSellerId() : null;

        if (buyerId != null && sellerId != null) {
            chatRoom.setIsJoin(2L);
        } else if (buyerId != null || sellerId != null) {
            chatRoom.setIsJoin(1L);
        } else {
            chatRoom.setIsJoin(0L);
        }
    }

    // 채팅방 ID로 조회
    public Optional<ChatRoom> findByChatRoomId(Long chatRoomId) {
        return chatRoomRepository.findByChatRoomId(chatRoomId);
    }

    public ChatRoom findChatRoomByChatRoomId(Long chatRoomId) {
        return chatRoomRepository.findChatRoomByChatRoomId(chatRoomId);
    }

    // 채팅방 나가면서 참여 여부 조회
    public Long leaveMessage(Long chatRoomId) {
        Optional<ChatRoom> optionalChatRoom = findByChatRoomId(chatRoomId);

        if (optionalChatRoom.isPresent()) {
            ChatRoom chatRoom = optionalChatRoom.get();
            updateChatRoomJoin(chatRoom); // 참여 상태 업데이트
            chatRoomRepository.save(chatRoom);  // 저장
            return chatRoom.getIsJoin(); // 참여 여부 반환
        } else {
            throw new RuntimeException("Chat room not found.");
        }
    }

    public Product findProductByChatRoomId(Long chatRoomId) {
        return chatRoomRepository.findProductByChatRoomId(chatRoomId);
    }

    // 마이페이지의 구매목록을 반환하는 메소드
    @Transactional(readOnly = true)
    public List<?> readByUserId(Long userId, String entityType, int sortType, String dealingStatus) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findByBuyerId_UserId(userId);
        List<Product> productList = new ArrayList<>();
        List<Car> carList = new ArrayList<>();

        if(entityType.equals("product")) {  // 중고 물품일때
            chatRoomList.forEach(chatRoom -> {
                if(chatRoom.getDealComplete()) productList.add(chatRoom.getProduct());
            });
            if(sortType == 1) productList.sort(Comparator.comparing(Product::getRegDate).reversed());  // 등록일시 내림차순
            else if(sortType == 2) productList.sort(Comparator.comparing(Product::getPrice));  // 가격 오름차순
            else productList.sort(Comparator.comparing(Product::getPrice).reversed());  // 가격 내림차순

            if(dealingStatus.equals("전체")) return productList;
            DealingStatus tempDS = DealingStatus.valueOf(dealingStatus);

            return productList
                    .stream()
                    .filter(product -> product.getDealingStatus().equals(tempDS))
                    .collect(Collectors.toList());
        } else if(entityType.equals("car")) {  // 중고차일때
            chatRoomList.forEach(chatRoom -> {
                if(chatRoom.getDealComplete()) carList.add(chatRoom.getCar());
            });
            if(sortType == 1) carList.sort(Comparator.comparing(Car::getRegDate).reversed());  // 등록일시 내림차순
            else if(sortType == 2) carList.sort(Comparator.comparing(Car::getPrice));  // 가격 오름차순
            else carList.sort(Comparator.comparing(Car::getPrice).reversed());  // 가격 내림차순

            if(dealingStatus.equals("전체")) return carList;
            DealingStatus tempDS = DealingStatus.valueOf(dealingStatus);

            return carList
                    .stream()
                    .filter(car -> car.getDealingStatus().equals(tempDS))
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Invalid entityType: " + entityType);
        }
    }
}