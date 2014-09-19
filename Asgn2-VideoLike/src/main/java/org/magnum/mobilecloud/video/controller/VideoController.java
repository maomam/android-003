package org.magnum.mobilecloud.video.controller;

import com.google.common.collect.Lists;
import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Controller
public class VideoController {

    /**
     * JPA Репозиторий для хранения наборов видео
     */
    @Autowired
    private VideoRepository videoRepository;

    @ResponseBody
    @RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.GET)
    public Collection<Video> getVideoList() {
        return Lists.newArrayList(videoRepository.findAll());
    }

    /**
     * Добавляет в набор новый объект
     * @param video Видео для добавления
     * @return Добавленное видео с расширенными параметрами
     */
    @ResponseBody
    @RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.POST)
    public Video addVideo(@RequestBody Video video) {

        // сбрасываем лайки
        video.setLikes(0);

        // сохраняем
        videoRepository.save(video);

        return video;
    }

    @ResponseBody
    @RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}", method = RequestMethod.GET)
    public Video getVideoById(
            @PathVariable("id") long id,
            HttpServletResponse response) {

        Video video = videoRepository.findOne(id);
        if (null == video) {
            response.setStatus(404);
        }

        return video;

    }

    @ResponseBody
    @RequestMapping(value = VideoSvcApi.VIDEO_TITLE_SEARCH_PATH, method = RequestMethod.GET)
    public Collection<Video> findByTitle(@RequestParam(VideoSvcApi.TITLE_PARAMETER) String title) {
        return videoRepository.findByName(title);
    }

    @ResponseBody
    @RequestMapping(value = VideoSvcApi.VIDEO_DURATION_SEARCH_PATH, method = RequestMethod.GET)
    public Collection<Video> findByDurationLessThan(@RequestParam(VideoSvcApi.DURATION_PARAMETER) long duration) {
        return videoRepository.findByDurationLessThan(duration);
    }

    @RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/like", method = RequestMethod.POST)
    public void likeVideo(
            @PathVariable("id") long id,
            Principal p,
            HttpServletResponse response) {

        Video video = videoRepository.findOne(id);

        // Смотрим, есть ли такое видео
        if (null == video) {
            response.setStatus(404);
            return;
        }

        // если есть - достаём список пользователей,
        // которые уже проглосовали, и проверяем, голосовал ли он уже?
        Set<String> likesUsernames = video.getLikesUsernames();

        String username = p.getName();

        if (likesUsernames.contains(username)) {

            response.setStatus(400);

        } else {

            likesUsernames.add(username);
            video.setLikesUsernames(likesUsernames);
            video.setLikes( likesUsernames.size() );
            videoRepository.save(video);

        }

    }

    @RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/unlike", method = RequestMethod.POST)
    public void unlikeVideo(
            @PathVariable("id") long id,
            Principal p,
            HttpServletResponse response) {

        Video video = videoRepository.findOne(id);

        // Смотрим, есть ли такое видео
        if (null == video) {
            response.setStatus(404);
            return;
        }

        // если есть - достаём список пользователей,
        // которые уже проглосовали, и проверяем, голосовал ли он уже?
        Set<String> likesUsernames = video.getLikesUsernames();

        String username = p.getName();

        if (likesUsernames.contains(username)) {

            likesUsernames.remove(username);
            video.setLikesUsernames(likesUsernames);
            video.setLikes(likesUsernames.size());
            videoRepository.save(video);

        } else {

            response.setStatus(400);

        }

    }

    @ResponseBody
    @RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/likedby", method = RequestMethod.GET)
    public Collection<String> getUsersWhoLikedVideo(
            @PathVariable("id") long id,
            HttpServletResponse response) {

        Collection<String> userNames = new ArrayList<>();
        Video video = videoRepository.findOne(id);

        if (null == video) {
            response.setStatus(404);
        } else {
            userNames.addAll(video.getLikesUsernames());
        }

        return userNames;

    }

}
