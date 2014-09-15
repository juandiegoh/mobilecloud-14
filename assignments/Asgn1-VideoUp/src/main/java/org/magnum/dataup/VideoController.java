/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.magnum.dataup;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
public class VideoController {

    private static final String HOST = "http://localhost:8080/video/%s/data";
    private Random idGenerator = new Random();

    private Map<Long, Video> videos = new HashMap<Long, Video>();

    @RequestMapping(value = "/video", method = RequestMethod.GET)
    public @ResponseBody Collection<Video> getVideos() {
        return this.videos.values();
    }

    @RequestMapping(value = "/video", method = RequestMethod.POST)
    public @ResponseBody Video addVideo(@RequestBody Video v) {
        Long newVideoId = this.getNewVideoId();
        v.setId(newVideoId);
        v.setDataUrl(String.format(HOST, newVideoId));
        this.saveVideo(v);
        return v;
    }

    @RequestMapping(value = "video/{id}/data", method = RequestMethod.POST)
    public @ResponseBody VideoStatus addVideoData(@PathVariable("id") Long id, MultipartFile data) throws IOException {
        Video v = this.getVideo(id);
        if(v != null) {
            InputStream in = data.getInputStream();
            VideoFileManager.get().saveVideoData(v, in);
            return new VideoStatus(VideoStatus.VideoState.READY);
        } else {
            throw new ResourceNotFoundException();
        }
    }


    @RequestMapping(value = "video/{id}/data", method = RequestMethod.GET)
    public void getVideoData(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        Video v = this.getVideo(id);
        if(v != null) {
            VideoFileManager.get().copyVideoData(v, response.getOutputStream());
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public Long getNewVideoId() {
        return Math.abs(this.idGenerator.nextLong() + 1);
    }

    private Video getVideo(Long id) {
        return this.videos.get(id);
    }

    private void saveVideo(Video v) {
        this.videos.put(v.getId(), v);
    }

}
