package gdd;


public class SpawnDetails {
    public String type;
    public int x;
    public int y;
    public int dx;
    public boolean mainWave;

    public SpawnDetails(String type, int x, int y, int dx) {
      this.type = type;
      this.x = x;
      this.y = y;
      this.dx = dx;
    }

    public SpawnDetails(String type, int x, int y, int dx, boolean mainWave) {
      this.type = type;
      this.x = x;
      this.y = y;
      this.dx = dx;
      this.mainWave = mainWave;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDx() {
        return dx;
    }
}
