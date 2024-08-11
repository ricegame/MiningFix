package ricedotwho.mf.data;

public class ApiData {
    public int professional;
    public int msBoost;
    public int potm;
    public ApiData(int professional, int msBoost, int potm) {
        this.professional = professional;
        this.msBoost = msBoost;
        this.potm = potm;
    }
    @Override
    public String toString() {
        return "Point"
                + "{"
                + "professional=" + this.professional
                + ",msBoost=" + this.msBoost
                + ",potm=" + this.potm
                + "}";
    }
}
