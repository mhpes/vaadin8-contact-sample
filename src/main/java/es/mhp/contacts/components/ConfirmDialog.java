package es.mhp.contacts.components;


import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;


import java.io.Serializable;

/**
 * Created by MHP on 20/04/2017.
 */
public class ConfirmDialog implements Serializable {
    private static final long serialVersionUID = 2344274165863515876L;
    static final String CONFIRM_TITLE = "Confirm";
    static final String CONFIRM_CANCEL_TITLE = "Cancel";
    static final String CONFIRM_OK_TITLE = "Ok";
//    static final int MAX_DESCRIPTION_LINES = 20;
    public static final String USER_CONFIRM_OK = "OK";
    public static final String USER_CONFIRM_CANCEL = "CANCEL";

    private UI win;
    private Window confirm;

    public ConfirmDialog(UI w) {
        win = w;
        confirm = null;
        if (win == null) {
            throw new IllegalStateException(
                    "User messages require a window instance");
        }
    }

//    public void error(String message) {
//        error(message, null, null);
//    }
//
//    public void error(String message, String description) {
//        error(message, description, null);
//    }
///
////    public void error(String message, String description, Throwable t) {
////        if (t != null) {
////            ByteArrayOutputStream stos = new ByteArrayOutputStream();
////            PrintStream sto = new PrintStream(stos, false);
////            t.printStackTrace(sto);
////            sto.flush();
////            try {
////                stos.flush();
////            } catch (IOException ignored) {
////            }
////            String st = stos.toString();
////            t.printStackTrace();
////            if (description == null) {
////                description = st;
////            } else {
////                description += ":\n" + st;
////            }
////        }
////        description = formatDescription(description);
////        win.showNotification(escapeXML(message), description,
////                Notification.TYPE_ERROR_MESSAGE);
////    }
////
    public void error(String message, Throwable t) {
//        error(message, null, t);
    }

//    public void error(Throwable e) {
//        error("Unhandled Exception", null, e);
//    }
//
//    public void warning(String message) {
//        warning(message, null);
//    }
//
//    public void warning(String message, String description) {
//        win.showNotification(message, formatDescription(description),
//                Notification.TYPE_WARNING_MESSAGE);
//    }
//
//    public void trayNotification(String message) {
//        trayNotification(message, null);
//    }
//
//    public void trayNotification(String message, String description) {
//        win.showNotification(message, formatDescription(description),
//                Notification.TYPE_TRAY_NOTIFICATION);
//    }
//
//    public void notification(String message) {
//        notification(message, null);
//    }
//
//    public void notification(String message, String description) {
//        win.showNotification(message, formatDescription(description));
//    }
//
//    public void alert(String message) {
//        alert(message, null);
//    }
//
//    public void alert(String message, String description) {
//        win.showNotification(message, formatDescription(description),
//                Notification.DELAY_FOREVER);
//    }
//
//    private String formatDescription(String description) {
//        if (description != null) {
//            description = escapeXML(description);
//            description = description.replaceAll("\n", "<br />");
//            if (description.length() > 80) {
//                String orig = description;
//                description = "";
//                while (orig.length() > 0) {
//                    int last = Math.min(80, orig.length());
//                    description += orig.substring(0, last);
//                    int lastnl = description.lastIndexOf("<br");
//                    int lastwb = description.lastIndexOf(' ');
//                    if (lastwb - lastnl > 10
//                            && lastwb < description.length() - 1) {
//                        description = description.substring(0, lastwb)
//                                + "<br />" + description.substring(lastwb);
//                    }
//                    orig = last == orig.length() ? "" : orig.substring(last);
//                }
//            }
//
//            // limit number of lines
//            int pos = description.indexOf("<br");
//            int lineCount = 1;
//            while (lineCount < MAX_DESCRIPTION_LINES && pos > 0
//                    && pos < description.length()) {
//                pos = description.indexOf("<br", pos + 3);
//                lineCount++;
//            }
//            if (pos > 0 && lineCount >= MAX_DESCRIPTION_LINES) {
//                description = description.substring(0, pos) + "<br />(...)";
//            }
//        }
//        return description;
//    }

    public Window confirm(String message, Button.ClickListener listener) {
        return confirm(CONFIRM_TITLE, message, CONFIRM_OK_TITLE,
                CONFIRM_CANCEL_TITLE, listener);
    }

    public Window confirm(String title, String message, Button.ClickListener listener) {
        return confirm(title, message, CONFIRM_OK_TITLE,
                CONFIRM_CANCEL_TITLE, listener);
    }

    public Window confirm(String title, String message, String okTitle,
                          String cancelTitle, Button.ClickListener listener) {

        // Check for default captions
        if (title == null) {
            title = CONFIRM_OK_TITLE;
        }
        if (cancelTitle == null) {
            cancelTitle = CONFIRM_CANCEL_TITLE;
        }
        if (okTitle == null) {
            okTitle = CONFIRM_OK_TITLE;
        }

        // Create a confirm dialog
        final Window confirm = new Window(title);
        this.confirm = confirm;
        win.addWindow(confirm);
        // Approximate the size of the dialog
        int chrW = 5;
        int chrH = 15;
        int txtWidth = Math.max(250, Math.min(350, message.length() * chrW));
        int btnHeight = 25;
        int vmargin = 100;
        int hmargin = 40;

        int txtHeight = 2 * chrH * (message.length() * chrW) / txtWidth;

        confirm.setWidth((txtWidth + hmargin) + "px");
        confirm.setHeight((vmargin + txtHeight + btnHeight) + "px");

        // Modal position in the center
        confirm.center();
        confirm.setModal(true);

        // Create content
        Label text = new Label(message);
        text.setWidth("100%");
        text.setHeight("100%");
        //h.expand(text, 1f);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setHeight(btnHeight + "px");
        buttons.setWidth("100%");
        Label spacer = new Label("");
        spacer.setWidth("100%");

        Button cancel = new Button(cancelTitle, listener);
        cancel.setData(USER_CONFIRM_CANCEL);
        cancel.setClickShortcut(KeyCode.ESCAPE);
        cancel.setStyleName(ValoTheme.BUTTON_DANGER);
        Button ok = new Button(okTitle, listener);
        ok.setData(USER_CONFIRM_OK);
        ok.setClickShortcut(KeyCode.ENTER);
        ok.setStyleName(ValoTheme.BUTTON_PRIMARY);
        buttons.addComponent(ok);
        buttons.addComponent(cancel);
        VerticalLayout v = new VerticalLayout();
        v.addComponent(text);
        v.addComponent(buttons);
        confirm.setContent(v);
        ((VerticalLayout)confirm.getContent()).setExpandRatio(text,1f);
        confirm.setResizable(false);
//        confirm.getContent().setSizeFull();
        return confirm;
    }

    public void removeConfirm() {
        if (this.confirm != null) {
            UI.getCurrent().removeWindow(confirm);
        }
    }

//    private String escapeXML(String str) {
//        return str == null ? null
//                : com.vaadin.terminal.gwt.server.JsonPaintTarget.escapeXML(str);
//    }
}


