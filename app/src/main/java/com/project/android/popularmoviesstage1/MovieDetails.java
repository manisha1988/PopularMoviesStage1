package com.project.android.popularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;

class MovieDetails implements Parcelable {

    private String movieId;

    private String imageUrl;

    private String orgTitle;

    private String plotSynopsis;

    private String userRating;

    private String releaseDate;

    /**
     * Setting all the values
     **/
    public MovieDetails(String movieId, String imageUrl,
                        String orgTitle, String plotSynopsis,
                        String userRating, String releaseDate) {
        this.movieId = movieId;
        this.imageUrl = imageUrl;
        this.orgTitle = orgTitle;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    /**
     * Retrieving Movie Details from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private MovieDetails(Parcel in) {
        this.movieId = in.readString();
        this.imageUrl = in.readString();
        this.orgTitle = in.readString();
        this.plotSynopsis = in.readString();
        this.userRating = in.readString();
        this.releaseDate = in.readString();
    }

    public static final Parcelable.Creator<MovieDetails> CREATOR = new Parcelable.Creator<MovieDetails>() {

        @Override
        public MovieDetails createFromParcel(Parcel source) {
            return new MovieDetails(source);
        }

        @Override
        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(imageUrl);
        dest.writeString(orgTitle);
        dest.writeString(plotSynopsis);
        dest.writeString(userRating);
        dest.writeString(releaseDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getOrgTitle() {
        return orgTitle;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

}
