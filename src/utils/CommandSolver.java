package utils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author lsyu
 */
public class CommandSolver extends Thread {

    private final long deltaTime;
    private final KeyTracker keyTracker;
    private final MouseTracker mouseTracker;
    private MouseListener ml;
    private KeyListener kl;

    private CommandSolver(final long updatesPerSecond, final KeyTracker keyTracker, final MouseTracker mouseTracker) {
        this.deltaTime = 1000000000 / updatesPerSecond;
        this.keyTracker = keyTracker;
        this.mouseTracker = mouseTracker;
    }

    public void observeMouse(final MouseListener ml) {
        this.ml = ml;
    }

    public void observeKeyBoard(final KeyListener kl) {
        this.kl = kl;
    }

    @Override
    public void run() {
        final long startTime = System.nanoTime();
        long passedFrames = 0;
        while (true) {
            final long currentTime = System.nanoTime();
            final long totalTime = currentTime - startTime;
            final long targetTotalFrames = totalTime / this.deltaTime;
            while (passedFrames < targetTotalFrames) {
                if (this.keyTracker != null) {
                    synchronized (this.keyTracker) {
                        this.keyTracker.record(currentTime);
                    }
                }
                if (this.mouseTracker != null) {
                    synchronized (this.mouseTracker) {
                        this.mouseTracker.record(currentTime);
                    }
                }
                passedFrames++;
            }
        }
    }

    public CommandWrapper update() {
        MouseData mouseData = null;
        KeyData keyData = null;
        if (this.keyTracker != null) {
            synchronized (this.keyTracker) {
                keyData = this.keyTracker.update();
            }
        }
        if (this.mouseTracker != null) {
            synchronized (this.mouseTracker) {
                mouseData = this.mouseTracker.update();
            }
        }

        final CommandWrapper cw = new CommandWrapper(keyData, mouseData);
        if (this.kl != null) {
            cw.actionCommand(this.kl);
        }
        if (this.ml != null) {
            cw.actionCommand(this.ml);
        }

        return cw;
    }

    public enum MouseState {
        CLICKED,
        PRESSED,
        RELEASED,
        ENTERED,
        EXITED,
        WHEEL_MOVED,
        DRAGGED,
        MOVED
    }

    public interface KeyCommandListener {

        public void keyPressed(int commandCode, long trigTime);

        public void keyReleased(int commandCode, long trigTime);
    }

    public interface TypedListener {

        public void keyTyped(char c, long trigTime);
    }

    public interface KeyListener extends KeyCommandListener, TypedListener {
    }

    public interface MouseListener {

        public void mouseTrig(MouseEvent e, MouseState state, long trigTime);
    }

    private static class Data {

        protected long time;

        public Data(final long time) {
            this.time = time;
        }
    }

    private static class KeyData extends Data {

        private Map<Byte, Boolean> map;
        private char key;

        public KeyData(final Map<Byte, Boolean> map, final long time) {
            super(time);
            this.map = map;
            this.key = 65535;
        }

        public KeyData(final Map<Byte, Boolean> map, final char key, final long time) {
            super(time);
            this.map = map;
            this.key = key;
        }
    }

    private static class MouseData extends Data {

        private MouseEvent e;
        private MouseState state;

        public MouseData(final MouseEvent e, final MouseState state, final long time) {
            super(time);
            this.e = e;
            this.state = state;
        }
    }

    public static class CommandWrapper {

        KeyData keyData;
        MouseData mouseData;

        private CommandWrapper(final KeyData keyData, final MouseData mouseData) {
            this.keyData = keyData;
            this.mouseData = mouseData;
        }

        public void actionCommand(final KeyCommandListener listener) {
            if (listener == null || this.keyData == null) {
                return;
            }
            this.keyData.map.keySet().forEach((key) -> {
                final boolean pressed = this.keyData.map.get(key);
                if (pressed) {
                    listener.keyPressed(key, this.keyData.time);
                } else {
                    listener.keyReleased(key, this.keyData.time);
                }
            });
        }

        public void actionCommand(final MouseListener listener) {
            if (listener == null || this.mouseData == null) {
                return;
            }
            listener.mouseTrig(this.mouseData.e, this.mouseData.state, this.mouseData.time);
        }

        public void actionCommand(final TypedListener listener) {
            if (listener == null || this.keyData == null || this.keyData.key == 65535) {
                return;
            }
            listener.keyTyped(this.keyData.key, this.keyData.time);
        }

        public void actionCommand(final KeyListener listener) {
            actionCommand((TypedListener) listener);
            actionCommand((KeyCommandListener) listener);
        }
    }

    public static class CommandConverter {

        private final Map<Integer, Byte> keyMap;// input to command
        // add @ 20191018 start
        private boolean isTrackChar;
        // add @ 20191018 end
        private char currentChar;
        private boolean clear;// 是否在更新時清除上一幀指令
        private boolean isKeyDeletion;// 更新時連鍵值都完整清除(不會觸發released)
        private Map<Byte, Boolean> pressedMap;// command pressed/released

        public CommandConverter(final boolean clear, final boolean isKeyDeletion, final boolean isTrackChar) {
            this.keyMap = new ConcurrentHashMap<>();
            this.pressedMap = new ConcurrentHashMap<>();
            this.clear = clear;
            this.isKeyDeletion = isKeyDeletion;
            this.isTrackChar = isTrackChar;
            if (isTrackChar) {
                this.currentChar = 65535;
            }
        }

        public void addKeyPair(final int key, final Byte command) {
            this.keyMap.put(key, command);
        }

        public void addKeyPair(final int key, final int command) {
            this.keyMap.put(key, (byte) command);
        }

        public void updateCommandByKey(final int key, final boolean pressed) {
            if (this.isTrackChar) {
                if (pressed && key >= 0 && key <= 255) {
                    this.currentChar = (char) key;
                } else {
                    this.currentChar = (char) 65535;
                }
            }
            if (!this.keyMap.containsKey(key)) {
                return;
            }
            this.pressedMap.put(this.keyMap.get(key), pressed);
        }

        public boolean getPressedByKey(final int key) {
            if (!this.keyMap.containsKey(key)) {
                return false;
            }
            return this.pressedMap.get(this.keyMap.get(key));
        }

        private Map<Byte, Boolean> getCurrentMap() {
            final Map<Byte, Boolean> tmp = this.pressedMap;
            if (this.clear) {
                this.pressedMap = new ConcurrentHashMap<>();
            } else {
                this.pressedMap = new ConcurrentHashMap<>(tmp);
                if (!this.isKeyDeletion) {
                    // fill map
                    this.keyMap.values().forEach((value) -> {
                        if (!tmp.containsKey(value)) {
                            tmp.put(value, Boolean.FALSE);
                        }
                    });
                } else {
                    // remove false
                    this.keyMap.values().forEach((value) -> {
                        if (this.pressedMap.containsKey(value) && !this.pressedMap.get(value)) {
                            this.pressedMap.remove(value);
                        }
                    });
                }
            }

            return tmp;
        }

        private char getCurrentKey() {
            final char tmp = this.currentChar;
            this.currentChar = 65535;
            return tmp;
        }

        public KeyData release(final long currentTime) {
            final Map<Byte, Boolean> t = getCurrentMap();
            if (this.isTrackChar) {
                return new KeyData(t, getCurrentKey(), currentTime);
            }
            return new KeyData(t, currentTime);
        }

        public KeyData release() {
            return release(0);
        }
    }

    private static class CommandRecorder<T extends Data> {

        private Node current;
        private Node tail;

        public CommandRecorder() {
            this.tail = this.current = new Node(null);
        }

        public void add(final T data) {
            this.tail = this.tail.next = new Node(data);
        }

        public boolean hasNext() {
            return this.current != this.tail;
        }

        public T next() {
            this.current = this.current.next;
            return this.current.data;
        }

        public T getCurrent() {
            return this.current.data;
        }

        public void reset() {
            this.current = new Node(null);
        }

        private class Node {

            private T data;
            private Node next;

            public Node(final T data) {
                this.data = data;
            }
        }

//        public void writeCsv(String fileName){
//
//        }
//
//        public static CommandRecorder genByCsv(String fileName){
//
//        }
    }

    private static class MouseTracker {

        private CommandRecorder<MouseData> recorder;
        private MouseEvent currentEvent;
        private MouseState currentState;
        private boolean isForcedReleased;

        private MouseTracker(final boolean isForcedReleased) {
            this.recorder = new CommandRecorder<>();
            this.isForcedReleased = isForcedReleased;
        }

        private void trig(final MouseEvent e, final MouseState state) {
            if (this.isForcedReleased && this.currentState == MouseState.RELEASED) {
                return;
            }
            this.currentEvent = e;
            this.currentState = state;
        }

        // 將當前的指令存入recorder並刷新(滑鼠暫時不刷新)指令集
        private void record(final long time) {
            this.recorder.add(new MouseData(this.currentEvent, this.currentState, time));
            this.currentEvent = null;
            this.currentState = null;
        }

        // 遊戲更新取得對應的指令
        public MouseData update() {
            if (this.recorder.hasNext()) {
                return this.recorder.next();
            }
            return null;
        }

        public void bind(final Component c) {
            final MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    trig(e, MouseState.CLICKED);
                }

                @Override
                public void mousePressed(final MouseEvent e) {
                    trig(e, MouseState.PRESSED);
                }

                @Override
                public void mouseReleased(final MouseEvent e) {
                    trig(e, MouseState.RELEASED);
                }

                @Override
                public void mouseEntered(final MouseEvent e) {
                    trig(e, MouseState.ENTERED);
                }

                @Override
                public void mouseExited(final MouseEvent e) {
                    trig(e, MouseState.EXITED);
                }

                @Override
                public void mouseWheelMoved(final MouseWheelEvent e) {
                    trig(e, MouseState.WHEEL_MOVED);
                }

                @Override
                public void mouseDragged(final MouseEvent e) {
                    trig(e, MouseState.DRAGGED);
                }

                @Override
                public void mouseMoved(final MouseEvent e) {
                    trig(e, MouseState.MOVED);
                }
            };
            c.addMouseListener(mouseAdapter);
            c.addMouseMotionListener(mouseAdapter);
            c.addMouseWheelListener(mouseAdapter);
        }
    }

    private static class KeyTracker {
        // 自定義行為列表

        private CommandConverter commandList;
        // 紀錄每一次更新時的按鍵行為
        private CommandRecorder<KeyData> recorder;

        // 導入行為列表
        private KeyTracker(final boolean clear, final boolean isKeyDeletion, final boolean isTrackChar) {
            this.commandList = new CommandConverter(clear, isKeyDeletion, isTrackChar);
            this.recorder = new CommandRecorder<>();
        }

        // 新增自定義按鍵以及對應的指令 => 用於不同種類的input設定
        public void add(final int key, final int command) {
            this.commandList.addKeyPair(key, command);
        }

        // 通過自定義按鍵去指定對應指令狀態
        public void trig(final int key, final boolean isPressed) {
            this.commandList.updateCommandByKey(key, isPressed);
        }

        // 將當前的指令存入recorder並刷新指令集
        public void record(final long time) {
            this.recorder.add(this.commandList.release(time));
        }

        // 遊戲更新取得對應的指令
        public KeyData update() {
            if (this.recorder.hasNext()) {
                return this.recorder.next();
            }
            return null;
        }

        public void bind(final Component c) {
            c.addKeyListener(new java.awt.event.KeyListener() {
                @Override
                public void keyTyped(final KeyEvent e) {
                }

                @Override
                public void keyPressed(final KeyEvent e) {
                    trig(e.getKeyCode(), true);
                }

                @Override
                public void keyReleased(final KeyEvent e) {
                    trig(e.getKeyCode(), false);
                }
            });
            c.setFocusable(true);
            c.requestFocusInWindow();
        }
    }

    public static class BuildStream {
//        private Component c;//mt.bind(c);
//        private long updatesPerSecond;

        private MouseOption mouseOption;
        private KeyOption keyOption;

        public MouseOption mouseTrack() {
            this.mouseOption = new MouseOption(this);
            return this.mouseOption;
        }

        public CommandMapListBuilder keyboardTrack() {
            this.keyOption = new KeyOption(this);
            return new CommandMapListBuilder(this.keyOption);
        }

        public CommandSolver bind(final Component c, final int updatesPerSecond) {
            KeyTracker kt = null;
            KeyListener kl = null;
            if (this.keyOption != null) {
                final KeyTracker tmp = new KeyTracker(this.keyOption.clear, this.keyOption.isKeyDeletion, this.keyOption.isTrackChar);
                if (this.keyOption.commandMapList != null) {
                    this.keyOption.commandMapList.foreach(keyPair -> {
                        tmp.add(keyPair[0], keyPair[1]);
                    });
                }
                tmp.bind(c);
                kt = tmp;
                kl = this.keyOption.kl;
            }

            MouseTracker mt = null;
            MouseListener ml = null;
            if (this.mouseOption != null) {
                mt = new MouseTracker(this.mouseOption.isForcedReleased);
                mt.bind(c);
                ml = this.mouseOption.ml;
            }

            final CommandSolver cs = new CommandSolver(updatesPerSecond, kt, mt);
            cs.observeKeyBoard(kl);
            cs.observeMouse(ml);

            return cs;
        }

        public static class CommandMapList {
            private ArrayList<int[]> cmArray;

            public CommandMapList() {
                this.cmArray = new ArrayList<>();
            }

            public CommandMapList add(final int key, final int command) {
                this.cmArray.add(new int[]{key, command});
                return this;
            }

            protected void foreach(final Consumer<int[]> consumer) {
                for (final int[] cmd : this.cmArray) {
                    consumer.accept(cmd);
                }
            }
        }

        public static class CommandMapListBuilder {
            private KeyOption src;
            private CommandMapList commandMapList;

            private CommandMapListBuilder(final KeyOption src) {
                this.src = src;
            }

            public KeyOption bind(final CommandMapList commandMapList) {
                this.commandMapList = commandMapList;
                return this.src;
            }

            public CommandMapListBuilder add(final int key, final int command) {
                if (this.commandMapList == null) {
                    this.commandMapList = new CommandMapList();
                }
                this.commandMapList.add(key, command);
                return this;
            }

            public KeyOption next() {
                this.src.setCommandMapList(this.commandMapList);
                return this.src;
            }
        }

        public static class KeyOption {
            private BuildStream src;
            private boolean isKeyTracker;
            private boolean clear;
            private boolean isKeyDeletion;
            private boolean isTrackChar;
            private KeyListener kl;
            private CommandMapList commandMapList;

            private KeyOption(final BuildStream src) {
                this.src = src;
            }

            private void setCommandMapList(final CommandMapList commandMapList) {
                this.commandMapList = commandMapList;
            }

            public KeyOption keyTypedMode() {
                this.clear = true;
                return this;
            }

            public KeyOption keyCleanMode() {
                // if keyTyped Mode open, clean mode banned
                if (!this.clear) {
                    this.isKeyDeletion = true;
                }
                return this;
            }

            public KeyOption trackChar() {
                this.isTrackChar = true;
                return this;
            }

            public BuildStream subscribe(final KeyListener kl) {
                this.kl = kl;
                return this.src;
            }
        }

        public static class MouseOption {
            private BuildStream src;
            private MouseListener ml;
            private boolean isForcedReleased;

            private MouseOption(final BuildStream src) {
                this.src = src;
            }

            public MouseOption forceRelease() {
                this.isForcedReleased = true;
                return this;
            }

            public BuildStream subscribe(final MouseListener ml) {
                this.ml = ml;
                return this.src;
            }
        }
    }

}
