package io.bgnc.SpringBootApplication.mapper;

import io.bgnc.SpringBootApplication.dto.SubredditDto;
import io.bgnc.SpringBootApplication.model.Post;
import io.bgnc.SpringBootApplication.model.Subreddit;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditDto(Subreddit subreddit);

    default  Integer mapPosts(List<Post> numberOfPosts){
        return numberOfPosts.size();
    }


    /**
     * SubredditMapperİmpl.
     * @param subredditDto
     * @return
     */
    @InheritConfiguration
    @Mapping(target = "posts",ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto);
}
