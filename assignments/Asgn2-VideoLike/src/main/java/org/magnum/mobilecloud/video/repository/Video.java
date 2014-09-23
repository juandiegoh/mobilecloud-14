package org.magnum.mobilecloud.video.repository;

import com.google.common.base.Objects;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple object to represent a video and its URL for viewing.
 * 
 * You probably need to, at a minimum, add some annotations to this
 * class.
 * 
 * You are free to add annotations, members, and methods to this
 * class. However, you probably should not change the existing
 * methods or member variables. If you do change them, you need
 * to make sure that they are serialized into JSON in a way that
 * matches what is expected by the auto-grader.
 * 
 * @author mitchell
 */
@Entity
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;
	private String url;
	private long duration;
	private long likes;

    @ElementCollection
    @JsonIgnore
	private List<String> usersWhoLiked = new ArrayList<String>();

	public Video() {
	}

	public Video(String name, String url, long duration, long likes) {
		super();
		this.name = name;
		this.url = url;
		this.duration = duration;
		this.likes = likes;
	}

    public boolean likeVideoFromUser(String userName) {
        if(usersWhoLiked.contains(userName)) {
            return false;
        } else {
            this.likes += 1;
            this.usersWhoLiked.add(userName);
            return true;
        }
    }

    public boolean unlikeVideoFromUser(String userName) {
        if(usersWhoLiked.contains(userName)) {
            this.likes -= 1;
            this.usersWhoLiked.remove(userName);
            return true;
        } else {
            return false;
        }
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLikes() {
		return likes;
	}

	public void setLikes(long likes) {
		this.likes = likes;
	}

	/**
	 * Two Videos will generate the same hashcode if they have exactly the same
	 * values for their name, url, and duration.
	 *
	 */
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(name, url, duration);
	}

	/**
	 * Two Videos are considered equal if they have exactly the same values for
	 * their name, url, and duration.
	 *
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Video) {
			Video other = (Video) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(name, other.name)
					&& Objects.equal(url, other.url)
					&& duration == other.duration;
		} else {
			return false;
		}
	}

    public List<String> getUsersWhoLiked() {
        return usersWhoLiked;
    }

    public void setUsersWhoLiked(List<String> usersWhoLiked) {
        this.usersWhoLiked = usersWhoLiked;
    }
}
