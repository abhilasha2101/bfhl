package com.example.bfhl.service.impl;

import com.example.bfhl.dto.BfhlRequest;
import com.example.bfhl.dto.BfhlResponse;
import com.example.bfhl.service.BfhlService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BfhlServiceImpl implements BfhlService {

    @Value("${bfhl.user.id}")
    private String userId;

    @Value("${bfhl.user.email}")
    private String email;

    @Value("${bfhl.user.roll-number}")
    private String rollNumber;

    @Override
    public BfhlResponse processData(BfhlRequest request) {
        List<String> data = request.getData();

        List<String> oddNumbers = new ArrayList<>();
        List<String> evenNumbers = new ArrayList<>();
        List<String> alphabets = new ArrayList<>();
        List<String> specialCharacters = new ArrayList<>();
        long sum = 0;

        for (String item : data) {
            if (isNumeric(item)) {
                long number = Long.parseLong(item);
                sum += number;
                if (number % 2 == 0) {
                    evenNumbers.add(item);
                } else {
                    oddNumbers.add(item);
                }
            } else if (isAlphabetic(item)) {
                alphabets.add(item.toUpperCase());
            } else {
                specialCharacters.add(item);
            }
        }

        String concatString = buildConcatString(data);

        return BfhlResponse.builder()
                .isSuccess(true)
                .userId(userId)
                .email(email)
                .rollNumber(rollNumber)
                .oddNumbers(oddNumbers)
                .evenNumbers(evenNumbers)
                .alphabets(alphabets)
                .specialCharacters(specialCharacters)
                .sum(String.valueOf(sum))
                .concatString(concatString)
                .build();
    }

    /**
     * Checks if a string represents a numeric value (integer).
     * Handles negative numbers as well.
     */
    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if a string contains only alphabetic characters.
     */
    private boolean isAlphabetic(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Concatenates all alphabetical characters from the input in reverse order
     * with alternating caps (first char uppercase, second lowercase, etc.).
     *
     * Logic:
     * 1. Collect all individual characters from alphabetic items
     * 2. Reverse the order
     * 3. Apply alternating caps (first = uppercase, second = lowercase, ...)
     */
    private String buildConcatString(List<String> data) {
        // Collect all individual alphabetical characters from the data
        StringBuilder allChars = new StringBuilder();
        for (String item : data) {
            if (isAlphabetic(item)) {
                allChars.append(item);
            }
        }

        if (allChars.length() == 0) {
            return "";
        }

        // Reverse the concatenated string
        String reversed = allChars.reverse().toString();

        // Apply alternating caps: index 0 = uppercase, index 1 = lowercase, ...
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            if (i % 2 == 0) {
                result.append(Character.toUpperCase(c));
            } else {
                result.append(Character.toLowerCase(c));
            }
        }

        return result.toString();
    }
}
