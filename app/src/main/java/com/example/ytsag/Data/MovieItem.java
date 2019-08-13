package com.example.ytsag.Data;

import java.util.List;

public class MovieItem
{
   private String name,date,imageUrl;
   private int id,duration;
   private String details;
   private List<String> genre;
   private String video_key;
   private String cast_name,cast_imagepath;


    public MovieItem(String name, String date, String imageUrl, int id) {
        this.name = name;
        this.date = date;
        this.imageUrl = imageUrl;
        this.id = id;
    }

    public String getCast_name() {
        return cast_name;
    }

    public String getCast_imagepath() {
        return cast_imagepath;
    }


    public MovieItem(String cast_name, String cast_imagepath) {
        this.cast_name = cast_name;
        this.cast_imagepath = cast_imagepath;

    }


    public String getDetails() {
        return details;
    }

    public int getDuration() {
        return duration;
    }

    public List<String> getGenre() {
        return genre;
    }

    public MovieItem(String name, String details, int duration, List<String> genre){
        this.name = name;
        this.details = details;
        this.duration = duration;
        this.genre = genre;

    }

    public MovieItem(String key){
        video_key = key;
    }

    public String getVideo_key() {
        return video_key;
    }

    public int getId() { return id; }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
