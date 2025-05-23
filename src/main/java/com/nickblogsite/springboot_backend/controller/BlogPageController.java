package com.nickblogsite.springboot_backend.controller;

import java.util.*;

import org.springframework.web.bind.annotation.*;

import com.nickblogsite.springboot_backend.entity.*;
import com.nickblogsite.springboot_backend.repository.*;

import lombok.*;

@RestController
@AllArgsConstructor
@RequestMapping( "/api" )
public class BlogPageController {

	private final BlogPageRepository blogPageRepository;
	private final UserRepository userRepository;

	@GetMapping( "/blogPage" )
	public List<BlogPage> fetchBlogs() {
		final List<BlogPage> blogPages = blogPageRepository.findAll();
		blogPages.sort( Comparator.comparingLong( BlogPage::getId ).reversed() );
		return blogPages;
	}

	@GetMapping( "/blogPagesFeatured" )
	public List<BlogPage> fetchBlogsFeatured() {
		final List<BlogPage> blogPages = blogPageRepository.getFeaturedBlogPages();
		blogPages.sort( Comparator.comparingLong( BlogPage::getId ).reversed() );
		return blogPages;
	}

	@GetMapping( "/blogPagesUnFeatured" )
	public List<BlogPage> fetchBlogsUnFeatured() {
		final List<BlogPage> blogPages = blogPageRepository.getUnFeaturedBlogPages();
		blogPages.sort( Comparator.comparingLong( BlogPage::getId ).reversed() );
		return blogPages;
	}

	@PostMapping( "/addBlogPage/{userID}" )
	@ResponseBody
	public BlogPage addBlogPage( @PathVariable( "userID" ) final String userID, @RequestBody final BlogPage newBlogPage ) {
		newBlogPage.setUser( userRepository.findById( Long.parseLong( userID ) ).orElse( null ) );
		return blogPageRepository.save( newBlogPage );
	}

	@PostMapping( "/deleteBlogPage/{id}" )
	public void deleteBlogPage( @PathVariable( "id" ) final String id ) {
		blogPageRepository.deleteById( Long.parseLong( id ) );
	}

	@GetMapping( "/getBlogPage/{id}" )
	public BlogPage getBlogPage( @PathVariable( "id" ) final String id ) {
		try {
			return blogPageRepository.findById( Long.parseLong( id ) ).orElse( null );
		} catch ( final NumberFormatException e ) {
			return new BlogPage();
		}
	}

	@PostMapping( "/markBlogPageFeatured/{id}" )
	public void markBlogPageFeatured( @PathVariable( "id" ) final String id ) {
		final BlogPage blogPage = blogPageRepository.getReferenceById( Long.parseLong( id ) );
		blogPage.setFeatured( true );
		blogPageRepository.save( blogPage );
	}

	@PostMapping( "/markBlogPageNotFeatured/{id}" )
	public void markBlogPageNotFeatured( @PathVariable( "id" ) final String id ) {
		final BlogPage blogPage = blogPageRepository.getReferenceById( Long.parseLong( id ) );
		blogPage.setFeatured( false );
		blogPageRepository.save( blogPage );
	}
}
