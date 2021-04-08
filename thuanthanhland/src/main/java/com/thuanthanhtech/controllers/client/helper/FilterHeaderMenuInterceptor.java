package com.thuanthanhtech.controllers.client.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerInterceptor;

import com.thuanthanhtech.controllers.client.utils.Filterable;
import com.thuanthanhtech.entities.Category;
import com.thuanthanhtech.repositories.CategoryRepository;

@Component
public class FilterHeaderMenuInterceptor implements HandlerInterceptor {

	@Autowired
	private CategoryRepository cRepository;

	private static List<String[]> RequestURINeedFilters;
	static {
		RequestURINeedFilters = getRequestURINeedFilter();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String requestURI = request.getRequestURI();
		
		// ===============================================================================
		boolean needFilterURI = RequestURINeedFilters.parallelStream().anyMatch((uris) -> {
			return Arrays.asList(uris).parallelStream().anyMatch((uri) -> {
				return (requestURI.startsWith(uri) && !uri.equals("/"));
			});
		});

		if (needFilterURI) {

			System.out.println("[INFO] " + FilterHeaderMenuInterceptor.class.getSimpleName() + ": Do filter request "
					+ requestURI);

			boolean canAccessiable = this.doFilter(request, response);

			if (!canAccessiable) {
				response.setStatus(HttpStatus.NOT_FOUND.value());
				response.sendError(HttpStatus.NOT_FOUND.value());
			}

			return canAccessiable;
		}

		return true;

	}

	public boolean doFilter(HttpServletRequest request, HttpServletResponse response) {
		String requestURI = request.getRequestURI();

		List<Category> privateCateList = cRepository.findByPub(0, null);

		boolean needFilterCateSlug = privateCateList.parallelStream().anyMatch((cate) -> {
			return (requestURI.contains(cate.getSlug()));
		});

		if (needFilterCateSlug) {
			System.err.println("[ERROR] " + FilterHeaderMenuInterceptor.class.getSimpleName()
					+ ": Don't allow access to " + requestURI);
			return false;
		}

		return true;
	}

	private static List<String[]> getRequestURINeedFilter() {

		List<String[]> requestURINeedFilters = Collections.synchronizedList(new ArrayList<String[]>());

		Reflections reflections = new Reflections("com.thuanthanhtech.controllers.client");

		Set<Class<? extends Filterable>> classes = reflections.getSubTypesOf(Filterable.class);
		classes.stream().forEach((c) -> {

			RequestMapping[] annotations = c.getAnnotationsByType(RequestMapping.class);
			Arrays.asList(annotations).stream().forEach((annotation) -> {
				if (annotation != null) {
					requestURINeedFilters.add(annotation.value());
				}
			});

		});

		return requestURINeedFilters;
	}

}
