import org.jcsp.lang.*;

import java.awt.geom.Point2D;

public class Gynimas {
    public static void main(String[] args) {
        int randomProcessAmount = 3;
        Any2OneChannelInt firstRandomToOwnerProcess = Channel.any2oneInt();
        Any2OneChannelInt secondRandomToOwnerProcess = Channel.any2oneInt();
        Any2OneChannelInt thirdRandomToOwnerProcess = Channel.any2oneInt();
        One2AnyChannelInt ownerProcessToRandomProcess = Channel.one2anyInt();
        One2AnyChannel<Point2D> ownerToFirstPrinterProcess = Channel.one2any();
        One2AnyChannel<Point2D> ownerToSecondPrinterProcess = Channel.one2any();
        Parallel parallel = new Parallel();
        OwnerProcess ownerProcess =
                new OwnerProcess(firstRandomToOwnerProcess.in(),
                        secondRandomToOwnerProcess.in(),
                        thirdRandomToOwnerProcess.in(),
                        ownerProcessToRandomProcess.out(),
                        ownerToFirstPrinterProcess.out(),
                        ownerToSecondPrinterProcess.out());
        RandomProcess firstRandomProcess =
                new RandomProcess(ownerProcessToRandomProcess.in(),
                        firstRandomToOwnerProcess.out(),
                        1);
        RandomProcess secondRandomProcess =
                new RandomProcess(ownerProcessToRandomProcess.in(),
                        secondRandomToOwnerProcess.out(),
                        2);
        RandomProcess thirdRandomProcess =
                new RandomProcess(ownerProcessToRandomProcess.in(),
                        thirdRandomToOwnerProcess.out(),
                        3);
        PrinterProcess firstPrinterProcess =
                new PrinterProcess(1, ownerToFirstPrinterProcess.in());
        PrinterProcess secondPrinterProcess =
                new PrinterProcess(2, ownerToSecondPrinterProcess.in());
        parallel.addProcess(firstRandomProcess);
        parallel.addProcess(secondRandomProcess);
        parallel.addProcess(thirdRandomProcess);
        parallel.addProcess(ownerProcess);
        parallel.addProcess(firstPrinterProcess);
        parallel.addProcess(secondPrinterProcess);
        parallel.run();
    }
}
