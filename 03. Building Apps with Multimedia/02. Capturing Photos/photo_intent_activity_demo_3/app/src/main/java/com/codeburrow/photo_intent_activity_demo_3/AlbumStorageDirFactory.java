package com.codeburrow.photo_intent_activity_demo_3;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since Jul/13/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */

import java.io.File;

abstract class AlbumStorageDirFactory {

    public abstract File getAlbumStorageDir(String albumName);
}
