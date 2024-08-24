package com.lec.spring.service;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.Car;
import com.lec.spring.domain.Product;
import com.lec.spring.domain.enums.DealingStatus;
import com.lec.spring.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CarService {
    private final CarRepository carRepository;
    private final UserService userService;
    private final AttachmentService attachmentService;

    // 기본적인 CRUD
    @Transactional
    public long create(Car car, Long userId, MultipartFile[] files) {
        car.setUser(userService.readOne(userId));
        car = carRepository.saveAndFlush(car);
        attachmentService.addFiles(files, car);
        return car.getCarId();
    }

    @Transactional
    public Car readOne(Long carId) {
        Car carEntity = carRepository.findById(carId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        carEntity.setViewCount(carEntity.getViewCount() + 1);
        return carRepository.save(carEntity);
    }

    @Transactional(readOnly = true)
    public List<Car> readAll() {
        return carRepository.findAll();
    }

    @Transactional
    public long update(Car car, Long carId, MultipartFile[] files, Long[] delfile) {
        Car carEntity = carRepository.findById(carId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));

        carEntity.setName(car.getName());
        carEntity.setPrice(car.getPrice());
        carEntity.setIntroduce(car.getIntroduce());
        carEntity.setCategory1(car.getCategory1());
        carEntity.setCategory2(car.getCategory2());
        carEntity.setStatus(car.getStatus());
        carEntity.setModelYear(car.getModelYear());
        carEntity.setCarNum(car.getCarNum());
        carEntity.setDistance(car.getDistance());
        carEntity.setDisplacement(car.getDisplacement());
        carEntity.setFuel(car.getFuel());
        carEntity.setTransmission(car.getTransmission());
        carEntity.setInsuranceVictim(car.getInsuranceVictim());
        carEntity.setInsuranceInjurer(car.getInsuranceInjurer());
        carEntity.setOwnerChange(car.getOwnerChange());

        carEntity = carRepository.saveAndFlush(carEntity);
        attachmentService.addFiles(files, carEntity);

        if(delfile != null) {
            for(Long fileId : delfile) {
                Attachment file = attachmentService.readOne(fileId);
                if(file != null) {
                    attachmentService.delFile(file);
                    attachmentService.delete(fileId);
                }
            }
        }

        return carId;
    }

    @Transactional
    public String delete(Long carId) {
        carRepository.deleteById(carId);
        return "ok";
    }

    // 추가 기능
    @Transactional
    public List<Car> getRandomCarsByCategory2(String category2) {
        List<Car> cars = carRepository.findCarByCategory2AndDealingStatus(category2, DealingStatus.판매중);
        if (cars.size() <= 5) {
            return cars;
        }

        // 리스트를 무작위로 섞고 5개 선택
        Collections.shuffle(cars);
        return cars.stream().limit(5).collect(Collectors.toList());
    }

    @Transactional

    public List<Car> getFilteredCars(String category1, String category2) {
        if(category1 != null && category2 != null) {
            return carRepository.findCarByCategory1AndCategory2OrderByRegDateDesc(category1, category2);
        } else if (category1 != null) {
            return carRepository.findCarByCategory1OrderByRegDateDesc(category1);
        }else
            return carRepository.findAll();

    }
    // 헤더에서 사용하는 검색 결과 불러오는 메소드
    @Transactional(readOnly = true)
    public List<Car> readAllByKeyword(String keyword) {
        List<Car> carList =  carRepository
                .findAll()
                .stream()
                .filter(car -> car.getName().contains(keyword))
                .collect(Collectors.toList());
        return carList;
    }

    // 마이페이지에서 사용하는 모든 필터 한 번에 걸러주는 메소드
    @Transactional(readOnly = true)
    public List<Car> readAllByUserSorted(Long userId, int sortType, String dealingStatus) {
        Sort sort;
        if(sortType == 1) sort = Sort.by(Sort.Order.desc("regDate"));
        else if(sortType == 2) sort = Sort.by(Sort.Order.asc("price"));
        else sort = Sort.by(Sort.Order.desc("price"));
        List<Car> carList = carRepository.findByUser_userId(userId, sort);

        if(dealingStatus.equals("전체")) return carList;
        DealingStatus tempDS = DealingStatus.valueOf(dealingStatus);
        return carList
                .stream()
                .filter(car -> car.getDealingStatus().equals(tempDS))
                .collect(Collectors.toList());
    }

//    @Transactional
//    public List<Car> getTop10ByViewCnt() {
//        return carRepository.findTop10ByDealingStatusOrderByRegDateDesc(DealingStatus.판매중);
//    }

    // 중고차 상세 페이지에서 사용하는 판매 상태 변경해주는 메소드
    @Transactional
    public Car updateDealingStatus(Long carId, DealingStatus dealingStatus) {
        Car carEntity = carRepository.findById(carId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        carEntity.setDealingStatus(dealingStatus);
        return carRepository.saveAndFlush(carEntity);
    }

    @Transactional
    public List<Car> getTop10ByViewCnt() {
        return carRepository.findTop10ByDealingStatusOrderByViewCountDesc(DealingStatus.판매중);
    }

    public Map<String, Object> getPriceInfoCarCategory(String category1, String category2) {
        List<Car> cars = carRepository.findCarsByCategories(category1 ,category2);

        // 가격 리스트
        List<Double> prices = new ArrayList<>();
        for (Car car : cars) {
            // 가격을 int에서 Double로 변환
            prices.add((double) car.getPrice());
        }

        // 평균, 최소, 최대 가격 계산
        double averagePrice = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double minPrice = prices.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
        double maxPrice = prices.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);

        // 결과를 Map으로 반환
        Map<String, Object> carInfo = new HashMap<>();
        carInfo.put("prices", prices);
        carInfo.put("averagePrice", averagePrice);
        carInfo.put("minPrice", minPrice);
        carInfo.put("maxPrice", maxPrice);
        carInfo.put("carCount", cars.size());

        return carInfo;
    }
}