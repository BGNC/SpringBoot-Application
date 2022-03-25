package io.bgnc.SpringBootApplication.service;

import io.bgnc.SpringBootApplication.dto.SubredditDto;
import io.bgnc.SpringBootApplication.exceptions.SpringBootApplicationException;
import io.bgnc.SpringBootApplication.mapper.SubredditMapper;
import io.bgnc.SpringBootApplication.model.Subreddit;
import io.bgnc.SpringBootApplication.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;


    @Transactional
    public SubredditDto save(SubredditDto subredditDto){

        Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));

        subredditDto.setId(save.getId());

        return subredditDto;
    }



    @Transactional(readOnly = true)
    public List<SubredditDto> getAll(){

        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditDto)
                .collect(Collectors.toList());


    }

    public SubredditDto getSubreddit(Long id){

        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(()->new SpringBootApplicationException("No suprable exception"+id));

        return subredditMapper.mapSubredditDto(subreddit);


    }





}
