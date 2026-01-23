package com.smartparking.user.config;

import com.smartparking.user.entity.User;
import com.smartparking.user.entity.Wallet;
import com.smartparking.user.repository.UserRepository;
import com.smartparking.user.repository.WalletRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Configuration
public class UserDataInitializer {

    private static final int USER_COUNT = 300;
    private static final String DEFAULT_PASSWORD = "password123";
    private static final double MIN_WALLET_BALANCE = 10.0;
    private static final double MAX_WALLET_BALANCE = 1000.0;

    private final Faker faker = new Faker(new Locale("en-US"));
    private final Random random = new Random();

    @Bean
    CommandLineRunner initUserDatabase(
            UserRepository userRepo,
            WalletRepository walletRepo,
            PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepo.count() > 0)
                return;
            System.out.println("Generating sample user data");

            List<User> users = seedUsers(userRepo, passwordEncoder);
            seedWallets(users, walletRepo);

            System.out.println("User Data Seeded Successfully!");
        };
    }

    private List<User> seedUsers(UserRepository repo, PasswordEncoder encoder) {
        List<User> users = new ArrayList<>();
        String encodedPw = encoder.encode(DEFAULT_PASSWORD);

        for (int i = 0; i < USER_COUNT; i++) {
            User user = new User();
            String username = faker.internet().username();
            user.setUsername(username);
            user.setPassword(encodedPw);
            user.setEmail(faker.internet().emailAddress(username));
            users.add(user);
        }
        return repo.saveAll(users);
    }

    private void seedWallets(List<User> users, WalletRepository repo) {
        List<Wallet> wallets = users.stream()
                .map(user -> new Wallet(user,
                        faker.number().randomDouble(2, (int) MIN_WALLET_BALANCE, (int) MAX_WALLET_BALANCE)))
                .toList();
        repo.saveAll(wallets);
    }
}
