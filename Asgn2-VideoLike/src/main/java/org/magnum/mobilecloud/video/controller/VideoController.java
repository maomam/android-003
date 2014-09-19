package org.magnum.mobilecloud.video.controller;

import com.google.common.collect.Lists;
import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

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
    public Collection<Video> findByTitle(@RequestParam("title") String title) {
        return videoRepository.findByName(title);
    }


//    @RequestMapping(value = "/video/{id}/data", method = RequestMethod.GET)
//    public void getVideoData(
//            @PathVariable("id") long id,
//            HttpServletResponse response) {
//        // Смотрим, есть ли такая запись
//        Video video = videos.get(id);
//        if (null == video) {
//            response.setStatus(404);
//        } else {
//            try {
//                VideoFileManager.get().copyVideoData(video, response.getOutputStream());
//                response.setStatus(200);
//            } catch (IOException e) {
//                if (e instanceof FileNotFoundException) {
//                    response.setStatus(404);
//                } else {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

//    @Multipart
//    @ResponseBody
//    @RequestMapping(value = "video/{id}/data", method = RequestMethod.POST)
//    public VideoStatus addVideoData(
//            @PathVariable("id") long id,
//            @RequestParam("data") MultipartFile data,
//            HttpServletResponse response) throws IOException {
//
//        VideoStatus status = null;
//
//        // Проверяем, есть ли метаданные для такого видео
//        Video video = videos.get(id);
//        if (null == video) {
//            response.setStatus(404);
//        } else {
//            VideoFileManager.get().saveVideoData(video, data.getInputStream());
//            status = new VideoStatus(VideoStatus.VideoState.READY);
//        }
//
//        return status;
//    }

}
