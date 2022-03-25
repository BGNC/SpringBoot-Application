package io.bgnc.SpringBootApplication.service;

import io.bgnc.SpringBootApplication.dto.SubredditDto;
import io.bgnc.SpringBootApplication.model.Subreddit;
import io.bgnc.SpringBootApplication.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;


    @Transactional
    public SubredditDto save(SubredditDto subredditDto){

        Subreddit save = subredditRepository.save(mapSubredditDto(subredditDto));

        subredditDto.setId(save.getId());

        return subredditDto;
    }

    private Subreddit mapSubredditDto(SubredditDto subredditDto) {

        return Subreddit.builder().name(subredditDto.getSubredditName())
                .description(subredditDto.getDescription())
                .build();
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll(){

        return subredditRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());


    }

    private SubredditDto mapToDto(Subreddit subreddit) {

        return SubredditDto.builder().subredditName(subreddit.getName()).id(subreddit.getId()).numberOfPost(subreddit.getPosts().size()).build();

    }



}
