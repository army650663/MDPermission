package tw.idv.madmanchen.mdpermissionlib;

import java.util.List;

/**
 * Author:      chenshaowei
 * Version      V1.0
 * Description:
 * Modification History:
 * Date         Author          version         Description
 * ---------------------------------------------------------------------
 * 2017/5/24      chenshaowei         V1.0.0          Create
 * Why & What is modified:
 */

public interface PermissionCallbacks {
    void success();

    void fail(List<String> deniedList);

    void alwaysDenied(List<String> alwaysDeniedList);
}
