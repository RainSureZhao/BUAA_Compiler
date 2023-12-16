/**
 * Created by 曾宪域 on 2023/12/13
 */
package backend.MipsCode;

public class Comment implements MipsCode {
    public final String comment;

    public Comment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "# " + comment;
    }
}
