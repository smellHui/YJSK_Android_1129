package com.tepia.guangdong_module.amainguangdong.wrap;

/**
 * Author:xch
 * Date:2019/11/27
 * Description:
 */
public class PatroltemEvent {

    private int position;
    private String problemDescription;
    private String imgPaths;

    public PatroltemEvent(int position, String problemDescription, String imgPaths) {
        this.position = position;
        this.problemDescription = problemDescription;
        this.imgPaths = imgPaths;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getImgPaths() {
        return imgPaths;
    }

    public void setImgPaths(String imgPaths) {
        this.imgPaths = imgPaths;
    }
}
