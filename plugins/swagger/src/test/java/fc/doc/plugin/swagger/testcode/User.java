package fc.doc.plugin.swagger.testcode;

import java.util.UUID;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 * Hello World
 */
@ApiModel
public class User {
    private UUID userUUID;
    private String userName;
    private String firstName;
    private String surname;

    @ApiModelProperty(position = 1, required = true, value = "User UUID")
    public UUID getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }

    @ApiModelProperty(position = 2, required = true, value = "username containing only lowercase letters")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @ApiModelProperty(position = 3, required = true)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @ApiModelProperty(position = 4, required = true)
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}