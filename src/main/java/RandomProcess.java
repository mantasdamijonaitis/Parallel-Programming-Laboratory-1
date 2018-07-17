import org.jcsp.lang.AltingChannelInputInt;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.SharedChannelInputInt;
import org.jcsp.lang.SharedChannelOutputInt;

public class RandomProcess implements CSProcess {

    private final SharedChannelInputInt inputFromOwnerProcess;
    private final SharedChannelOutputInt outputToOwnerProcess;
    private final int index;

    public RandomProcess(SharedChannelInputInt inputFromOwnerProcess,
                         SharedChannelOutputInt outputToOwnerProcess,
                         int index) {
        this.inputFromOwnerProcess = inputFromOwnerProcess;
        this.outputToOwnerProcess = outputToOwnerProcess;
        this.index = index;
    }

    @Override
    public void run() {
        boolean finishWriting = false;
        while (!finishWriting) {
            outputToOwnerProcess.write(index);
            int feedbackFromOwner = inputFromOwnerProcess.read();
            finishWriting = feedbackFromOwner == -1;
        }
    }
}
