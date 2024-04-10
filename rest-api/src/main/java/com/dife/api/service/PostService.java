package com.dife.api.service;

import com.dife.api.exception.MemberNotFoundException;
import com.dife.api.model.BOARD_category;
import com.dife.api.model.Member;
import com.dife.api.model.Post;
import com.dife.api.model.dto.BoardDto;
import com.dife.api.model.dto.PostCreateRequestDto;

import com.dife.api.repository.MemberRepository;
import com.dife.api.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    public Post create(PostCreateRequestDto requestDto, Member currentMember) {

        Post post = new Post();
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        post.setIs_public(requestDto.getIs_public());
        post.setBoardType(requestDto.getBoardType());
        post.setMember(currentMember);

        this.postRepository.save(post);

        return post;
    }

    public List<BoardDto> getPostsByBoardType(BOARD_category boardType) {
        List<Post> posts = postRepository.findPostsByBoardType(boardType);

        return posts.stream()
                .map(BoardDto::new)
                .collect(Collectors.toList());
    }

    public Post getPost(Long id)
    {
        Post post = postRepository.findById(id).orElseThrow(()
            -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다!"));

        return post;
    }
}
