package com.example.bfhl.service;

import com.example.bfhl.dto.BfhlRequest;
import com.example.bfhl.dto.BfhlResponse;

public interface BfhlService {

    /**
     * Processes the input data array and returns categorized results.
     *
     * @param request the request containing the data array
     * @return response with categorized numbers, alphabets, special chars, etc.
     */
    BfhlResponse processData(BfhlRequest request);
}
