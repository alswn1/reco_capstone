package com.example.kmj_reco.DTO;

public class TRASHCAN {
        public int trash_num; // trashcan Index
        public String trashcan_roadname; // 도로명
        public String trashcan_address; // 주소
        public String trashcan_locate; // 위치
        public String trashcan_fulladdress; // 상세주소
        public int Latitude; // 위도
        public int Longitude; // 경도

        public TRASHCAN() {}

        public TRASHCAN(int trash_num, String trashcan_roadname, String trashcan_address, String trashcan_locate, int Latitude,  int Longitude,String trashcan_fulladdress) {
            this.trash_num = trash_num;
            this.trashcan_roadname = trashcan_roadname;
            this.trashcan_address = trashcan_address;
            this.trashcan_locate = trashcan_locate;
            this.trashcan_fulladdress = trashcan_fulladdress;
            this.Latitude = Latitude;
            this.Longitude = Longitude;
        }

        public int getTrash_num() {
            return trash_num;
        }
        public void setTrash_num(int trash_num) {
            this.trash_num = trash_num;
        }

        public String getTrashcan_roadname() {
            return trashcan_roadname;
        }
        public void setTrashcan_roadname(String trashcan_roadname) { this.trashcan_roadname = trashcan_roadname; }

        public String getTrashcan_address() {
            return trashcan_address;
        }
        public void setTrashcan_address(String trashcan_address) { this.trashcan_address = trashcan_address; }

        public String getTrashcan_locate() { return trashcan_locate; }
        public void setTrashcan_locate(String trashcan_locate) { this.trashcan_locate = trashcan_locate; }

        public String getTrashcan_fulladdress() {
        return trashcan_fulladdress;
    }
        public void setTrashcan_fulladdress(String trashcan_fulladdress) { this.trashcan_fulladdress = trashcan_fulladdress; }

        public int getLatitude() {
        return Latitude;
    }
        public void setLatitude(int Latitude) {
        this.Latitude = Latitude;
    }

        public int getLongitude() {
        return Longitude;
    }
        public void setLongitude(int Longitude) { this.Longitude = Longitude; }

        @Override
        public String toString() {
            return "TRASHCAN{" +
                    "trash_num=" + trash_num +
                    ", trashcan_roadname='" + trashcan_roadname + '\'' +
                    ", trashcan_address='" + trashcan_address + '\'' +
                    ", trashcan_locate" + trashcan_locate + '\'' +
                    ", trashcan_fulladdress" + trashcan_fulladdress + '\'' +
                    ", Latitude" + Latitude + '\'' +
                    ", Longitude" + Longitude + '\'' +
                    '}';
        }
}
