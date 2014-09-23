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

package org.magnum.mobilecloud.video;

import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;

@Controller
public class VideoController {

    @Autowired
    VideoRepository videoRepository;
	
	@RequestMapping(value="/go",method=RequestMethod.GET)
	public @ResponseBody String goodLuck(){
		return "Good Luck!";
	}

    @RequestMapping(value="/video",method=RequestMethod.POST)
    public @ResponseBody Video addVideo(@RequestBody Video video){
        return this.videoRepository.save(video);
    }

    @RequestMapping(value="/video",method=RequestMethod.GET)
    public @ResponseBody Iterable<Video> getVideos(){
        return videoRepository.findAll();
    }

    @RequestMapping(value="video/search/findByName", method=RequestMethod.GET)
    public @ResponseBody Collection<Video> findByTitle(final @RequestParam String title) {
        return this.videoRepository.findByName(title);
    }

    @RequestMapping(value="video/search/findByDurationLessThan", method=RequestMethod.GET)
    public @ResponseBody Collection<Video> findByDurationLessThan(final @RequestParam long duration) {
        return this.videoRepository.findByDurationLessThan(duration);
    }

    @RequestMapping(value="/video/{id}/like", method=RequestMethod.POST)
    public ResponseEntity likeVideo(@PathVariable("id") long id, Principal p) {
        Video video = this.videoRepository.findOne(id);
        if(video == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            if(video.likeVideoFromUser(p.getName())) {
                this.videoRepository.save(video);
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @RequestMapping(value="/video/{id}/unlike", method=RequestMethod.POST)
    public ResponseEntity unlikeVideo(@PathVariable("id") long id, Principal p) {
        Video video = this.videoRepository.findOne(id);
        if(video == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            if(video.unlikeVideoFromUser(p.getName())) {
                this.videoRepository.save(video);
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @RequestMapping(value="/video/{id}", method=RequestMethod.GET)
    public @ResponseBody Video getVideoById(@PathVariable("id") long id) {
        Video video = this.videoRepository.findOne(id);
        if(video != null) {
            return video;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @RequestMapping(value="/video/{id}/likedby", method=RequestMethod.GET)
    public @ResponseBody Collection<String> getUsersWhoLiked(@PathVariable("id") long id) {
        Video video = this.videoRepository.findOne(id);
        if(video != null) {
            return video.getUsersWhoLiked();
        } else {
            throw new ResourceNotFoundException();
        }
    }

    //    @ExceptionHandler
    //    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //    public void handle(HttpMessageNotReadableException e) {
    //        String a = e.getMessage();
    //    }

}
