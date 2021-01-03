package shanglei.net_wuziqi.program.wuziqi;

import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.TextArea;

public class PanelMessage extends Panel {
    public TextArea mesageArea;

    public PanelMessage() {
        setLayout(new BorderLayout());
        mesageArea = new TextArea("", 12, 20, TextArea.SCROLLBARS_VERTICAL_ONLY);
        add(mesageArea, BorderLayout.CENTER);
    }
}
