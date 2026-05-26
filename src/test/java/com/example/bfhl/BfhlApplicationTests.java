package com.example.bfhl;

import com.example.bfhl.dto.BfhlRequest;
import com.example.bfhl.dto.BfhlResponse;
import com.example.bfhl.service.BfhlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BfhlApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BfhlService bfhlService;

    // ==================== Controller Integration Tests ====================

    @Test
    @DisplayName("POST /bfhl - Example A: Mixed data with numbers, letters, and special chars")
    void testExampleA() throws Exception {
        BfhlRequest request = new BfhlRequest(Arrays.asList("a", "1", "334", "4", "R", "$"));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.user_id").isNotEmpty())
                .andExpect(jsonPath("$.odd_numbers[0]").value("1"))
                .andExpect(jsonPath("$.even_numbers[0]").value("334"))
                .andExpect(jsonPath("$.even_numbers[1]").value("4"))
                .andExpect(jsonPath("$.alphabets[0]").value("A"))
                .andExpect(jsonPath("$.alphabets[1]").value("R"))
                .andExpect(jsonPath("$.special_characters[0]").value("$"))
                .andExpect(jsonPath("$.sum").value("339"))
                .andExpect(jsonPath("$.concat_string").value("Ra"));
    }

    @Test
    @DisplayName("POST /bfhl - Example B: Multiple numbers and special chars")
    void testExampleB() throws Exception {
        BfhlRequest request = new BfhlRequest(Arrays.asList("2", "a", "y", "4", "&", "-", "*", "5", "92", "b"));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.odd_numbers[0]").value("5"))
                .andExpect(jsonPath("$.even_numbers[0]").value("2"))
                .andExpect(jsonPath("$.even_numbers[1]").value("4"))
                .andExpect(jsonPath("$.even_numbers[2]").value("92"))
                .andExpect(jsonPath("$.alphabets[0]").value("A"))
                .andExpect(jsonPath("$.alphabets[1]").value("Y"))
                .andExpect(jsonPath("$.alphabets[2]").value("B"))
                .andExpect(jsonPath("$.special_characters[0]").value("&"))
                .andExpect(jsonPath("$.special_characters[1]").value("-"))
                .andExpect(jsonPath("$.special_characters[2]").value("*"))
                .andExpect(jsonPath("$.sum").value("103"))
                .andExpect(jsonPath("$.concat_string").value("ByA"));
    }

    @Test
    @DisplayName("POST /bfhl - Example C: Multi-character alphabetic strings")
    void testExampleC() throws Exception {
        BfhlRequest request = new BfhlRequest(Arrays.asList("A", "ABCD", "DOE"));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.odd_numbers").isEmpty())
                .andExpect(jsonPath("$.even_numbers").isEmpty())
                .andExpect(jsonPath("$.alphabets[0]").value("A"))
                .andExpect(jsonPath("$.alphabets[1]").value("ABCD"))
                .andExpect(jsonPath("$.alphabets[2]").value("DOE"))
                .andExpect(jsonPath("$.special_characters").isEmpty())
                .andExpect(jsonPath("$.sum").value("0"))
                .andExpect(jsonPath("$.concat_string").value("EoDdCbAa"));
    }

    @Test
    @DisplayName("POST /bfhl - Empty data array returns empty results")
    void testEmptyData() throws Exception {
        BfhlRequest request = new BfhlRequest(Collections.emptyList());

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.odd_numbers").isEmpty())
                .andExpect(jsonPath("$.even_numbers").isEmpty())
                .andExpect(jsonPath("$.alphabets").isEmpty())
                .andExpect(jsonPath("$.special_characters").isEmpty())
                .andExpect(jsonPath("$.sum").value("0"))
                .andExpect(jsonPath("$.concat_string").value(""));
    }

    @Test
    @DisplayName("POST /bfhl - Only numbers")
    void testOnlyNumbers() throws Exception {
        BfhlRequest request = new BfhlRequest(Arrays.asList("1", "2", "3", "4", "5"));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.odd_numbers[0]").value("1"))
                .andExpect(jsonPath("$.odd_numbers[1]").value("3"))
                .andExpect(jsonPath("$.odd_numbers[2]").value("5"))
                .andExpect(jsonPath("$.even_numbers[0]").value("2"))
                .andExpect(jsonPath("$.even_numbers[1]").value("4"))
                .andExpect(jsonPath("$.alphabets").isEmpty())
                .andExpect(jsonPath("$.special_characters").isEmpty())
                .andExpect(jsonPath("$.sum").value("15"))
                .andExpect(jsonPath("$.concat_string").value(""));
    }

    @Test
    @DisplayName("POST /bfhl - Invalid request (null data) returns 400")
    void testNullData() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    @Test
    @DisplayName("POST /bfhl - Malformed JSON returns 400")
    void testMalformedJson() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("invalid json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    @Test
    @DisplayName("GET /bfhl - Returns operation code 1")
    void testGetOperationCode() throws Exception {
        mockMvc.perform(get("/bfhl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation_code").value(1));
    }

    // ==================== Service Unit Tests ====================

    @Test
    @DisplayName("Service - Processes mixed data correctly")
    void testServiceProcessData() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("a", "1", "334", "4", "R", "$"));
        BfhlResponse response = bfhlService.processData(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getOddNumbers()).containsExactly("1");
        assertThat(response.getEvenNumbers()).containsExactly("334", "4");
        assertThat(response.getAlphabets()).containsExactly("A", "R");
        assertThat(response.getSpecialCharacters()).containsExactly("$");
        assertThat(response.getSum()).isEqualTo("339");
        assertThat(response.getConcatString()).isEqualTo("Ra");
    }

    @Test
    @DisplayName("Service - concat_string with multi-char words")
    void testServiceConcatString() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("A", "ABCD", "DOE"));
        BfhlResponse response = bfhlService.processData(request);

        // All chars: A + ABCD + DOE = AABCDDOE
        // Reversed: EODDCBAA
        // Alternating caps (0=upper,1=lower,...): EoDdCbAa
        assertThat(response.getConcatString()).isEqualTo("EoDdCbAa");
    }

    @Test
    @DisplayName("Service - Negative numbers handled correctly")
    void testNegativeNumbers() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("-3", "-4", "0"));
        BfhlResponse response = bfhlService.processData(request);

        assertThat(response.getOddNumbers()).containsExactly("-3");
        assertThat(response.getEvenNumbers()).containsExactly("-4", "0");
        assertThat(response.getSum()).isEqualTo("-7");
    }

    @Test
    @DisplayName("Service - Large numbers handled correctly")
    void testLargeNumbers() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("999999999", "1"));
        BfhlResponse response = bfhlService.processData(request);

        assertThat(response.getSum()).isEqualTo("1000000000");
        assertThat(response.getOddNumbers()).containsExactly("999999999", "1");
        assertThat(response.getEvenNumbers()).isEmpty();
    }

    @Test
    @DisplayName("Service - Only special characters")
    void testOnlySpecialCharacters() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("@", "#", "$", "%"));
        BfhlResponse response = bfhlService.processData(request);

        assertThat(response.getSpecialCharacters()).containsExactly("@", "#", "$", "%");
        assertThat(response.getOddNumbers()).isEmpty();
        assertThat(response.getEvenNumbers()).isEmpty();
        assertThat(response.getAlphabets()).isEmpty();
        assertThat(response.getSum()).isEqualTo("0");
        assertThat(response.getConcatString()).isEmpty();
    }

    @Test
    @DisplayName("Service - User ID is populated")
    void testUserIdPopulated() {
        BfhlRequest request = new BfhlRequest(List.of("1"));
        BfhlResponse response = bfhlService.processData(request);

        assertThat(response.getUserId()).isNotNull();
        assertThat(response.getUserId()).isNotEmpty();
        assertThat(response.getEmail()).isNotNull();
        assertThat(response.getRollNumber()).isNotNull();
    }
}
