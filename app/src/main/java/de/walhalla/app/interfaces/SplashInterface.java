package de.walhalla.app.interfaces;

/**
 * @author B3tterTogeth3r
 * @since 1.0
 * @version 1.0
 * @implNote <b>DO NOT IMPLEMENT ANYWHERE EXCEPT {@link de.walhalla.app.SplashActivity SPLASHACTIVITY}</b>
 * @implSpec <b>ONLY IMPLEMENTED BY {@link de.walhalla.app.SplashActivity SPLASHACTIVITY}</b>
 */
public interface SplashInterface {
    /** only use in the constructor of {@link de.walhalla.app.App App} */
    void appDone();
    /** only use in the constructor of {@link de.walhalla.app.firebase.Firebase Firebase} */
    void firebaseDone();
}
