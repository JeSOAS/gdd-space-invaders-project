package gdd;


public class SpawnDetails {
    public String type;
    public int x;
    public int y;
    public boolean mainWave;

    public SpawnDetails(String type, int x, int y) {
      this.type = type;
      this.x = x;
      this.y = y;
    }

    public SpawnDetails(String type, int x, int y, boolean mainWave) {
      this.type = type;
      this.x = x;
      this.y = y;
      this.mainWave = mainWave;
    }
}
