package ricedotwho.mf.data;

import com.google.gson.JsonObject;

public class ApiData {
    public int professional;
    public int msBoost;
    public int potm;
    public JsonObject pet;
    public int pet_lvl;
    public double bal_buff;
    public ApiData(int professional, int msBoost, int potm, JsonObject pet, int pet_lvl, double bal_buff) {
        this.professional = professional;
        this.msBoost = msBoost;
        this.potm = potm;
        this.pet = pet;
        this.pet_lvl = pet_lvl;
        this.bal_buff = bal_buff;
    }
    @Override
    public String toString() {
        return "ApiData"
                + "{"
                + "professional=" + this.professional
                + ",msBoost=" + this.msBoost
                + ",potm=" + this.potm
                + ",pet=" + this.pet
                + ",pet_lvl=" + this.pet_lvl
                + ",bal_buff=" + this.bal_buff
                + "}";
    }
}
