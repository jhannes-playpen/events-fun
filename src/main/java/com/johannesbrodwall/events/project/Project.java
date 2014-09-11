package com.johannesbrodwall.events.project;

import org.json.JSONObject;

import com.johannesbrodwall.infrastructure.web.JSONConvertible;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@ToString(exclude={"parent", "children"})
@EqualsAndHashCode(exclude={"parent", "children"})
public class Project implements JSONConvertible {

    @Getter @Setter
    private Integer id;

    @Getter
    private final String displayName;

    public void setParent(Project parent) {
        this.parentId = parent.getId();
        this.parent = parent;
    }

    @Getter @Setter
    private List<Project> children = new ArrayList<Project>();

    void setParentId(Integer parentId) {
        this.parentId = parentId > 0 ? parentId : null;
    }

    private Integer parentId;

    @Getter
    private Project parent;

    @Override
    public JSONObject toJSON() {
        return new JSONObject()
            .put("id", id)
            .put("displayName", displayName)
            .put("parentName", getParentName());
    }

    private String getParentName() {
        return parent == null ? "" : parent.getDisplayName();
    }

    public static Project fromJSON(JSONObject object) {
        Project project = new Project(object.getString("displayName"));
        if (object.has("parent") && object.get("parent") != JSONObject.NULL) {
            project.parentId = object.getInt("parent");
        }
        return project;
    }

    public Integer getParentId() {
        return parent != null ? parent.getId() : parentId;
    }

}
