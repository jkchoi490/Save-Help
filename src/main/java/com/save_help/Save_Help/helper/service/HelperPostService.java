package com.save_help.Save_Help.helper.service;

import com.save_help.Save_Help.helper.dto.HelperCommentRequestDto;
import com.save_help.Save_Help.helper.dto.HelperCommentResponseDto;
import com.save_help.Save_Help.helper.dto.HelperPostRequestDto;
import com.save_help.Save_Help.helper.dto.HelperPostResponseDto;
import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.entity.HelperComment;
import com.save_help.Save_Help.helper.entity.HelperPost;
import com.save_help.Save_Help.helper.repository.HelperCommentRepository;
import com.save_help.Save_Help.helper.repository.HelperPostRepository;
import com.save_help.Save_Help.helper.repository.HelperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HelperPostService {

    private final HelperRepository helperRepository;
    private final HelperPostRepository postRepository;
    private final HelperCommentRepository commentRepository;

    @Transactional
    public HelperPostResponseDto createPost(HelperPostRequestDto dto) {
        Helper helper = helperRepository.findById(dto.getHelperId())
                .orElseThrow(() -> new IllegalArgumentException("Helper를 찾을 수 없습니다."));

        HelperPost post = HelperPost.builder()
                .author(helper)
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        postRepository.save(post);
        return toPostDto(post);
    }

    @Transactional(readOnly = true)
    public List<HelperPostResponseDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::toPostDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HelperPostResponseDto getPost(Long postId) {
        HelperPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        post.increaseViewCount();
        return toPostDto(post);
    }

    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Transactional
    public HelperCommentResponseDto addComment(Long postId, HelperCommentRequestDto dto) {
        HelperPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        Helper helper = helperRepository.findById(dto.getHelperId())
                .orElseThrow(() -> new IllegalArgumentException("Helper를 찾을 수 없습니다."));

        HelperComment comment = HelperComment.builder()
                .post(post)
                .author(helper)
                .content(dto.getContent())
                .build();

        commentRepository.save(comment);

        return HelperCommentResponseDto.builder()
                .id(comment.getId())
                .authorName(helper.getName())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    private HelperPostResponseDto toPostDto(HelperPost post) {
        List<HelperCommentResponseDto> commentDtos = post.getComments().stream()
                .map(c -> HelperCommentResponseDto.builder()
                        .id(c.getId())
                        .authorName(c.getAuthor().getName())
                        .content(c.getContent())
                        .createdAt(c.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return HelperPostResponseDto.builder()
                .id(post.getId())
                .authorName(post.getAuthor().getName())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .comments(commentDtos)
                .build();
    }
}
