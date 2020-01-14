package myin.phone.views.settings.toolbar;

import lombok.Getter;
import myin.phone.data.tool.HomeTool;

import java.util.HashMap;
import java.util.Map;

public class PredefinedTools {

    @Getter
    private Map<Tool, HomeTool> tools = new HashMap<Tool, HomeTool>() {{
    }};

    public enum Tool {
        PHONE,
    }
}
