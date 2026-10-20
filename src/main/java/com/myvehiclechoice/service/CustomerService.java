package com.myvehiclechoice.service;

import com.myvehiclechoice.dto.VehicleSummaryDto;
import com.myvehiclechoice.exception.ResourceNotFoundException;
import com.myvehiclechoice.model.entity.SavedComparison;
import com.myvehiclechoice.model.entity.User;
import com.myvehiclechoice.model.entity.Vehicle;
import com.myvehiclechoice.model.entity.Wishlist;
import com.myvehiclechoice.repository.SavedComparisonRepository;
import com.myvehiclechoice.repository.UserRepository;
import com.myvehiclechoice.repository.VehicleRepository;
import com.myvehiclechoice.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final WishlistRepository wishlistRepository;
    private final SavedComparisonRepository comparisonRepository;
    private final VehicleService vehicleService;

    public CustomerService(UserRepository userRepository,
                           VehicleRepository vehicleRepository,
                           WishlistRepository wishlistRepository,
                           SavedComparisonRepository comparisonRepository,
                           VehicleService vehicleService) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.wishlistRepository = wishlistRepository;
        this.comparisonRepository = comparisonRepository;
        this.vehicleService = vehicleService;
    }

    public void addToWishlist(String userEmail, Long vehicleId) {
        User user = getUser(userEmail);
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + vehicleId));

        if (!wishlistRepository.existsByUserIdAndVehicleId(user.getId(), vehicleId)) {
            wishlistRepository.save(new Wishlist(user, vehicle));
        }
    }

    public void removeFromWishlist(String userEmail, Long vehicleId) {
        User user = getUser(userEmail);
        wishlistRepository.deleteByUserIdAndVehicleId(user.getId(), vehicleId);
    }

    @Transactional(readOnly = true)
    public List<VehicleSummaryDto> getUserWishlist(String userEmail) {
        User user = getUser(userEmail);
        return wishlistRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(w -> vehicleService.toSummaryDto(w.getVehicle()))
                .collect(Collectors.toList());
    }

    public SavedComparison saveComparison(String userEmail, String title, String variantIds) {
        User user = getUser(userEmail);
        SavedComparison comp = new SavedComparison(user, title, variantIds);
        return comparisonRepository.save(comp);
    }

    @Transactional(readOnly = true)
    public List<SavedComparison> getUserSavedComparisons(String userEmail) {
        User user = getUser(userEmail);
        return comparisonRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    private User getUser(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}
