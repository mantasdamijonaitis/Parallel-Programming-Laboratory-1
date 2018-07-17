import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.SharedChannelInput;

import java.awt.geom.Point2D;

public class PrinterProcess implements CSProcess{

    private final int index;
    private final SharedChannelInput<Point2D> inputFromOwnerProcess;

    public PrinterProcess(int index,
                          SharedChannelInput<Point2D> inputFromOwnerProcess) {
        this.index = index;
        this.inputFromOwnerProcess = inputFromOwnerProcess;
    }

    @Override
    public void run() {
        boolean continueLooping = true;
        while (continueLooping){
            Point2D receivedData = inputFromOwnerProcess.read();
            if (receivedData == null) {
                continueLooping = false;
            } else {
                System.out.println("Proceso nr: " + index + " M: " + receivedData.getX() + " N: " + receivedData.getY());
            }
        }
    }
}
