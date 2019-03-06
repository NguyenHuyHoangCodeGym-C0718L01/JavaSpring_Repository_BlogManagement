package com.codegym.cms.controller;

import com.codegym.cms.model.Category;
import com.codegym.cms.model.Post;
import com.codegym.cms.service.CategoryService;
import com.codegym.cms.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CategoryService categoryService;

    @ModelAttribute("categories")
    public Iterable<Category> findAllCategory(){
        return categoryService.findAll();
    }

    @GetMapping("/create-blog")
    public ModelAndView showCreateForm(){
        ModelAndView modelAndView = new ModelAndView("/post/create");
        modelAndView.addObject("post", new Post());
        return modelAndView;
    }

    @PostMapping("/create-blog")
    public ModelAndView saveBlog(@ModelAttribute("post") Post post){
        postService.save(post);

        ModelAndView modelAndView = new ModelAndView("/post/create");
        modelAndView.addObject("post", new Post());
        modelAndView.addObject("message", "New post created successfully");
        return modelAndView;
    }

    @GetMapping("/blog")
    public ModelAndView listBlogs(@RequestParam("nameBlog") Optional<String> name, Pageable pageable, @RequestParam("sortType")Optional<String> sortType){
        Page<Post> posts;
        if(name.isPresent()){
            if(sortType.isPresent()){
                Sort sort;
                if(sortType.equals("ASC")){
                    sort = new Sort(Sort.Direction.ASC, "id");
                }else{
                    sort = new Sort(Sort.Direction.DESC, "id");

                }
                Iterable<Post> myPosts = postService.findAllByName(name.get(), sort);
                posts = postService.convert(myPosts, pageable);
            }else{
                posts = postService.findAllByName(name.get(), pageable);
            }
        } else {
            if(sortType.isPresent()){
                Sort sort;
                if(sortType.equals("ASC")){
                    sort = new Sort(Sort.Direction.ASC, "id");
                }else{
                    sort = new Sort(Sort.Direction.DESC, "id");
                }
                Iterable<Post> myPosts = postService.findAll(sort);
                posts = postService.convert(myPosts, pageable);
            }else {
                posts = postService.findAll(pageable);
            }
        }

        ModelAndView modelAndView = new ModelAndView("/post/list");
        modelAndView.addObject("posts", posts);
        return modelAndView;
    }

    @GetMapping("/edit-blog/{id}")
    public ModelAndView showEditForm(@PathVariable int id){
        Post post = postService.findById(id);
        if(post != null) {
            ModelAndView modelAndView = new ModelAndView("/post/edit");
            modelAndView.addObject("post", post);
            return modelAndView;

        }else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/edit-blog")
    public ModelAndView updateBlog(@ModelAttribute("post") Post post){
        postService.save(post);
        ModelAndView modelAndView = new ModelAndView("/post/edit");
        modelAndView.addObject("post", post);
        modelAndView.addObject("message", "Blog updated successfully");
        return modelAndView;
    }

    @GetMapping("/delete-blog/{id}")
    public ModelAndView showDeleteForm(@PathVariable int id){
        Post post = postService.findById(id);
        if(post != null) {
            ModelAndView modelAndView = new ModelAndView("/post/delete");
            modelAndView.addObject("post", post);
            return modelAndView;

        }else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/delete-blog")
    public String deleteBlog(@ModelAttribute("post") Post post){
        postService.remove(post.getId());
        return "redirect:blog";
    }

    @GetMapping("/view-blog/{id}")
    public ModelAndView showBlogDetail(@PathVariable int id){
        Post post = postService.findById(id);
        if(post!= null){
            ModelAndView modelAndView = new ModelAndView("/post/view");
            modelAndView.addObject("post", post);
            return modelAndView;
        }else{
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }
    @GetMapping("/posts/{name}")
    public ModelAndView showBlogByCategory(@PathVariable String categoryName, Pageable pageable, @RequestParam("sortType")Optional<String> sortType){
        Page<Post> posts;
        if(sortType.isPresent()){
            Sort sort;
            if(sortType.equals("ASC")){
                sort = new Sort(Sort.Direction.ASC, "id");
            }else{
                sort = new Sort(Sort.Direction.DESC, "id");
            }
            Iterable<Post> myPosts = postService.findAllByCategoryName(categoryName, sort);
            posts = postService.convert(myPosts, pageable);
        }else{
            posts = postService.findAllByCategoryName(categoryName, pageable);
        }
        ModelAndView modelAndView = new ModelAndView("/post/list");
        modelAndView.addObject("posts", posts);
        return modelAndView;
    }
}
