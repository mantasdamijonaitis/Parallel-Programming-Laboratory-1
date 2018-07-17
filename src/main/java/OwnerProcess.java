import org.jcsp.lang.*;

import java.awt.*;
import java.awt.geom.Point2D;

public class OwnerProcess implements CSProcess {

    private static final int TOTAL_PROCESS_AMOUNT = 3;

    private int m = 100;
    private int n = 0;

    private int changeAmount = 0;

    private final AltingChannelInputInt firstRandomToOwnerProcessInput;
    private final AltingChannelInputInt secondRandomToOwnerProcessInput;
    private final AltingChannelInputInt thirdRandomToOwnerProcessInput;
    private final ChannelOutputInt outputToRandomProcess;
    private final ChannelOutput<Point2D> outputToFirstPrinterProcess;
    private final ChannelOutput<Point2D> outputToSecondPrinterProcess;

    public OwnerProcess(AltingChannelInputInt firstRandomToOwnerProcessInput,
                        AltingChannelInputInt secondRandomToOwnerProcessInput,
                        AltingChannelInputInt thirdRandomToOwnerProcessInput,
                        ChannelOutputInt outputToRandomProcess,
                        ChannelOutput<Point2D> outputToFirstPrinterProcess,
                        ChannelOutput<Point2D> outputToSecondPrinterProcess) {
        this.firstRandomToOwnerProcessInput = firstRandomToOwnerProcessInput;
        this.secondRandomToOwnerProcessInput = secondRandomToOwnerProcessInput;
        this.thirdRandomToOwnerProcessInput = thirdRandomToOwnerProcessInput;
        this.outputToRandomProcess = outputToRandomProcess;
        this.outputToFirstPrinterProcess = outputToFirstPrinterProcess;
        this.outputToSecondPrinterProcess = outputToSecondPrinterProcess;
    }

    @Override
    public void run() {

        Guard[] guard = new Guard[TOTAL_PROCESS_AMOUNT + 1];
        guard[0] = firstRandomToOwnerProcessInput;
        guard[1] = secondRandomToOwnerProcessInput;
        guard[2] = thirdRandomToOwnerProcessInput;
        guard[3] = new Skip();
        Alternative alternative = new Alternative(guard);

        boolean[] preCondition = new boolean[TOTAL_PROCESS_AMOUNT + 1];
        preCondition[0] = true;
        preCondition[1] = true;
        preCondition[2] = true;
        preCondition[3] = false;

        boolean loopNeeded = true;
        while (loopNeeded) {
            int stateNumber = alternative.fairSelect(preCondition);
            int receivedIndex = -1;
            switch (stateNumber) {
                case 0:
                    receivedIndex = firstRandomToOwnerProcessInput.read();
                    break;
                case 1:
                    receivedIndex = secondRandomToOwnerProcessInput.read();
                    break;
                case 2:
                    receivedIndex = thirdRandomToOwnerProcessInput.read();
                    break;
            }
            n += receivedIndex;
            m -= receivedIndex;
            changeAmount++;
            if (changeAmount % 2 == 0) {
                Point2D dataToWrite = new Point(m,n);
                this.outputToFirstPrinterProcess.write(dataToWrite);
                this.outputToSecondPrinterProcess.write(dataToWrite);
            }
            if (Math.abs(n - m) > 5) {
                outputToRandomProcess.write(0);
            } else {
                outputToRandomProcess.write(-1);
                loopNeeded = false;
            }
        }
    }

}
