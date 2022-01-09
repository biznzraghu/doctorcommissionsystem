package org.nh.artha.domain.dto;

import java.io.Serializable;
import java.util.Map;

public class GroupMemberDto implements Serializable{

    private static final long serialVersionUID = 1L;

    private Map<String,Object> member;

    private boolean inactive;

    public GroupMemberDto() {
    }

    public GroupMemberDto(Map<String,Object> member, boolean inactive) {
        this.member = member;
        this.inactive = inactive;
    }

    public Object getMember() {
        return member;
    }

    public void setMember(Map<String,Object> member) {
        this.member = member;
    }

    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        GroupMemberDto member1 = (GroupMemberDto) o;

        return member.equals(member1.member);
    }

    @Override
    public int hashCode() {
        return member.hashCode();
    }

    @Override
    public String toString() {
        return "GroupMember{" +
            "member=" + member +
            ", inactive=" + inactive +
            '}';
    }
}
