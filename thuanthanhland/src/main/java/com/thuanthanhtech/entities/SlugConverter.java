package com.thuanthanhtech.entities;

import java.text.Normalizer;
import java.util.regex.Pattern;

import org.springframework.context.annotation.Bean;

public class SlugConverter {
	
	@Bean
	public static String convert(String value) {
        try { 
            value = customNormalizer(value);
        	String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

            return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll("[:\"'-`]", " ").replaceAll(" ", "-").replaceAll("đ", "d");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
	
	 public static String customNormalizer(String str) {
		    str = str.replaceAll("à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a");
		    str = str.replaceAll("è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e");
		    str = str.replaceAll("ì|í|ị|ỉ|ĩ", "i");
		    str = str.replaceAll("ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ", "o");
		    str = str.replaceAll("ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u");
		    str = str.replaceAll("ỳ|ý|ỵ|ỷ|ỹ", "y");
		    str = str.replaceAll("đ", "d");

		    str = str.replaceAll("À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ", "A");
		    str = str.replaceAll("È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ", "E");
		    str = str.replaceAll("Ì|Í|Ị|Ỉ|Ĩ", "I");
		    str = str.replaceAll("Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ", "O");
		    str = str.replaceAll("Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ", "U");
		    str = str.replaceAll("Ỳ|Ý|Ỵ|Ỷ|Ỹ", "Y");
		    str = str.replaceAll("Đ", "D");
		    return str;
	 }
}
