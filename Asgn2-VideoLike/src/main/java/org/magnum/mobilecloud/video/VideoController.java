package org.magnum.mobilecloud.video;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import retrofit.http.Multipart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class VideoController {

    private static final AtomicLong lastVideoId = new AtomicLong(0L);

    /**
     * Набор добавленных записей.
     * Используем специальный тип коллекции, которая корректно работает
     * в многопоточной среде.
     */
    private Map<Long,Video> videos = new ConcurrentHashMap<>();

    /**
     * Запрос на все существующие записи
     * @return Набор добавленных записей
     */
    @ResponseBody
    @RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.GET)
    public Collection<Video> getVideoList() {
        return videos.values();
    }

//    /**
//     * Добавляет в набор новый объект
//     * @param video Видео для добавления
//     * @return Добавленное видео с расширенными параметрами
//     */
//    @ResponseBody
//    @RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.POST)
//    public Video addVideo(@RequestBody Video video) {
//
//        long id = lastVideoId.incrementAndGet();
//
//        // Идентификатор
//        video.setId(id);
//
//        // Cсылка
//        generateUrl(video);
//
//        // добавляем
//        videos.put(id, video);
//
//        return video;
//    }

    @RequestMapping(value = "/video/{id}/data", method = RequestMethod.GET)
    public void getVideoData(
            @PathVariable("id") long id,
            HttpServletResponse response) {
        // Смотрим, есть ли такая запись
        Video video = videos.get(id);
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
    }

    @Multipart
    @ResponseBody
    @RequestMapping(value = "video/{id}/data", method = RequestMethod.POST)
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

    /**
     * Формирует полную ссылку для последующего доступа к записи
     * @param video Объект, для которого формируется ссылка
     */
//    private void generateUrl(Video video) {
//        String url = getUrlBaseForLocalServer() + "/video/" + video.getId() + "/data";
//        video.setDataUrl(url);
//    }

    private String getUrlBaseForLocalServer() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return "http://"+request.getServerName()
                + ((request.getServerPort() != 80) ? ":"+request.getServerPort() : "");
    }

}
