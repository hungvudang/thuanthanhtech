package com.thuanthanhtech.controllers.client;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thuanthanhtech.controllers.client.helper.CategoryClientHelper;
import com.thuanthanhtech.controllers.client.utils.Filterable;

@Controller
@RequestMapping(value = "/ve-chung-toi")
public class AboutClientController implements Filterable {

	@Autowired
	private CategoryClientHelper cateClientHelper;

	@GetMapping
	public String about(Model m, HttpServletRequest request, HttpServletResponse response) {

		List<String> breadcrumbs = new ArrayList<String>();
		breadcrumbs.add("Về chúng tôi");
		m.addAttribute("breadcrumbs", breadcrumbs);

		cateClientHelper.categories(m);

		return "/public-pages/about";
	}
}
