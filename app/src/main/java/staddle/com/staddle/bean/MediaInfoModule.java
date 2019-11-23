package staddle.com.staddle.bean;

public class MediaInfoModule {
    private String uid;

    private String username;

    private String status;

    private String email;

    private String createdate;

    private String image;

    private String mobile;

    public String getUid ()
    {
        return uid;
    }

    public void setUid (String uid)
    {
        this.uid = uid;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getCreatedate ()
    {
        return createdate;
    }

    public void setCreatedate (String createdate)
    {
        this.createdate = createdate;
    }

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }

    public String getMobile ()
    {
        return mobile;
    }

    public void setMobile (String mobile)
    {
        this.mobile = mobile;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [uid = "+uid+", username = "+username+", status = "+status+", email = "+email+", createdate = "+createdate+", image = "+image+", mobile = "+mobile+"]";
    }
}
