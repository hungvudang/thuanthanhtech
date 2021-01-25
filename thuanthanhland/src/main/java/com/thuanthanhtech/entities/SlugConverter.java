package com.thuanthanhtech.entities;

import java.text.Normalizer;
import java.util.regex.Pattern;

import org.springframework.context.annotation.Bean;

public class SlugConverter {
	
	@Bean
	public static String convert(String value) {
        try {
            String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

            return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll("\\s+", " ").replaceAll(" ", "-")
                    .replaceAll("đ", "d").replaceAll("Đ", "D");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
