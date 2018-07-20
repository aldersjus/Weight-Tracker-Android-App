package jp.blogspot.jusoncode.weighttracker;


class Weight implements Comparable{

    int id;
    String date;
    String weight;
    String measurement;

    Weight(String date, String weight,String measurement){
        this.date = date;
        this.weight = weight;
        this.measurement = measurement;
    }

    @Override
    public int compareTo(Object o) {
        Weight another = (Weight)o;

        if(another.id > this.id){
            return 1;
        }else if(another.id < this.id){
            return -1;
        }else {
            return 0;
        }
    }
}
