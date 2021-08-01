package T18A_Group6_A2;

public class Masker implements Runnable{

    private boolean stop;

    public Masker(String prompt) {
        System.out.print(prompt);
    }

    @Override
    public void run() {
        stop = true;
        while (stop) {
            System.out.print("\010*");
            try {
                Thread.sleep(1);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopMasking() {
        this.stop = false;
    }
    
    public boolean returnStop() { return stop; }
}
