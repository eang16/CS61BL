

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    /**
     * The max image depth level.
     */
    public static final int MAX_DEPTH = 7;
    private static Image upperLeft = null;
    private static Image upperRight = null;
    private static Image downLeft = null;
    private static Image downRight = null;

    public RasterResultParams getMapRaster(RasterRequestParams params) {
        double rootL = MapServer.ROOT_ULLON;
        double rootR = MapServer.ROOT_LRLON;
        double rootU = MapServer.ROOT_ULLAT;
        double rootD = MapServer.ROOT_LRLAT;
        if (params.lrlat > params.ullat || params.ullon > params.lrlon) {
            return RasterResultParams.queryFailed();
        }
        if (params.lrlon < MapServer.ROOT_ULLON || params.ullon > MapServer.ROOT_LRLON) {
            return RasterResultParams.queryFailed();
        }
        if (params.lrlat > MapServer.ROOT_ULLAT || params.ullat < MapServer.ROOT_LRLAT) {
            return RasterResultParams.queryFailed();
        }
        RasterResultParams.Builder r = new RasterResultParams.Builder();

        double dpp = lonDPP(params.lrlon, params.ullon, params.w);
        double fpp = dpp * 288200;
        int depth = 0;

        int leftx = 0;
        int rightx = 0;
        int upy = 0;
        int downy = 0;

//        int depth = findDepth(params.ullon, params.ullat, params.lrlon, params.lrlat, dpp, 0);
        for (int i = 0; i < 8; i++) {
            double a = params.ullon;
            double b = params.ullat;
            double c = params.lrlon;
            double d = params.lrlat;
            int[] imageDim = findDepth(a, b, c, d, dpp, i);
            if (imageDim[0] != 11) {
                depth = imageDim[0];
                leftx = imageDim[1];
                rightx = imageDim[2];
                upy = imageDim[3];
                downy = imageDim[4];

                break;
            }
        }

        if (depth == 0) {
            String[][] imageGrid = new String[1][1];
            imageGrid[0][0] = "d0_x0_y0";
            r.setRasterLrLat(params.lrlat);
            r.setRasterUlLat(params.ullat);
            r.setRasterLrLon(params.lrlon);
            r.setRasterUlLon(params.ullon);
            r.setQuerySuccess(true);
            r.setRenderGrid(imageGrid);
            r.setDepth(depth);

        } else {

            String[][] imageGrid = new String[downy - upy + 1][rightx - leftx + 1];
            for (int i = 0; i < (downy - upy + 1); i++) {
                for (int j = 0; j < (rightx - leftx + 1); j++) {
                    imageGrid[i][j] = "d" + depth + "_x" + (leftx + j) + "_y" + (upy + i) + ".png";
                }
            }
            double[] newParameters = calcParams(depth, leftx, rightx, upy, downy); //left right up down
//            r.setRasterUlLon(newParameters[0]);
//            r.setRasterLrLon(newParameters[1]);
//            r.setRasterUlLat(newParameters[2]);
//            r.setRasterLrLat(newParameters[3]);
            r.setRasterUlLon(upperLeft.leftx);
            r.setRasterLrLon(upperRight.rightx);
            r.setRasterUlLat(upperLeft.upy);
            r.setRasterLrLat(downRight.downy);


            r.setQuerySuccess(true);
            r.setRenderGrid(imageGrid);
            r.setDepth(depth);
        }
        return r.create();

    }


    private int[] findDepth(double ullon, double ullat, double lrlon,
                            double lrlat, double dpp, int depthLevel) {


        int[] dimList = new int[5];

        if (depthLevel == 0) {
            double newdpp = ((MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / 256);
            if (newdpp <= dpp) {
                dimList[0] = 0;
                dimList[1] = 0;
                dimList[2] = 0;
                dimList[3] = 0;
                dimList[4] = 0;
                return dimList;
            } else {
                dimList[0] = 11;
                return dimList;
            }
        }

        dimList[0] = depthLevel;


        Image[][] input = imageLongLat(depthLevel);

        upperLeft = null;
        upperRight = null;
        downLeft = null;
        downRight = null;


        int rowStart = 0;
        int rowEnd = 0;
        int colStart = 0;
        int colEnd = 0;

        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                if (upperLeft == null) {
                    if (input[i][j].isWithin(ullon, ullat)) {
                        upperLeft = input[i][j];
                        //colStart = i;
                        dimList[1] = j;
                        //rowStart = j;
                        dimList[3] = i;
                    }
                }
                if (upperRight == null) {
                    if (input[i][j].isWithin(lrlon, ullat)) {
                        upperRight = input[i][j];
                        //colEnd = i;
                        dimList[2] = j;
                    }
                }
                if (downLeft == null) {
                    if (input[i][j].isWithin(ullon, lrlat)) {
                        downLeft = input[i][j];
                        //colEnd = j;
                        dimList[4] = i;
                    }
                }
                if (downRight == null) {
                    if (input[i][j].isWithin(lrlon, lrlat)) {
                        downRight = input[i][j];
                    }
                }

                //  cornercases

                if (upperLeft == null) {
                    if (input[i][j].isWithinOrLeft(ullon, ullat)) {
                        upperLeft = input[i][j];
                        //colStart = i;
                        dimList[1] = j;
                        //rowStart = j;
                        dimList[3] = i;
                    }
                }
                if (upperRight == null && j == input[i].length - 1) {
                    if (input[i][j].isWithin(lrlon, ullat)) {
                        upperRight = input[i][j];
                        //colEnd = i;
                        dimList[2] = j;
                    }
                }
                if (downLeft == null && i == input.length - 1) {
                    if (input[i][j].isWithin(ullon, lrlat)) {
                        downLeft = input[j][i];
                        //colEnd = j;
                        dimList[4] = i;
                    }
                }
                if (downRight == null) {
                    if (input[i][j].isWithin(lrlon, lrlat)) {
                        downRight = input[i][j];
                    }
                }
            }
        }

        double depthdpp;
//        double depthdpp1;
//        double depthdpp2;


        if (dimList[2] == dimList[1]) {
            depthdpp = (upperRight.rightx - upperLeft.leftx) / (256);
        } else {
//            depthdpp1 = (upperRight.rightx - upperLeft.leftx) / (256 * (dimList[2] - dimList[1]));
//            depthdpp2 = (upperLeft.upy - downRight.downy) / (256 * (dimList[2] - dimList[1]));
//            if(depthdpp1<depthdpp2){
//                depthdpp = depthdpp1;
//            } else {
//                depthdpp = depthdpp2;
//            }
            depthdpp = (upperRight.rightx - upperLeft.leftx) / (256 * (dimList[2] - dimList[1] + 1));
        }

        if (depthLevel == 7) {
            dimList[0] = depthLevel;
            return dimList;

        }
        if (depthdpp <= dpp) {
            dimList[0] = depthLevel;
            return dimList;

        } else {
            dimList[0] = 11;
            return dimList;
        }


    }

    private Image[][] imageLongLat(int depthLevel) {
        double rootL = MapServer.ROOT_ULLON;
        double rootR = MapServer.ROOT_LRLON;
        double rootU = MapServer.ROOT_ULLAT;
        double rootD = MapServer.ROOT_LRLAT;
        Image[][] imageList = new Image[(int) Math.pow(2, depthLevel)]
                [(int) Math.pow(2, depthLevel)];
        double constantX = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON)
                / Math.pow(2, depthLevel);
        double constantY = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT)
                / Math.pow(2, depthLevel);

        for (int i = 0; i < imageList.length; i++) {
            for (int j = 0; j < imageList[i].length; j++) {
                imageList[i][j] = new Image(MapServer.ROOT_ULLON + (constantX * j),
                        MapServer.ROOT_ULLON + (constantX * (j + 1)),
                        MapServer.ROOT_ULLAT - (constantY * i),
                        MapServer.ROOT_ULLAT - (constantY * (i + 1)));
            }
        }
        return imageList;
    }
//
//    private Image[][] imageLongLat2(int depthLevel){
//        Image[][] imageList = new Image[(int) Math.pow(2, depthLevel)][(int) Math.pow(2, depthLevel)];
//        double constantX = (4 - 0)/ Math.pow(2, depthLevel);
//        double  constantY = (4 - 0)/ Math.pow(2, depthLevel);
//
//        for(int i = 0; i < imageList.length; i++){
//            for(int j = 0; j<imageList[i].length; j++){
//                imageList[i][j] = new Image(0+(constantX*j),
//                        0+(constantX*(j+1)),
//                        4-(constantY*i),
//                        4-(constantX*(i+1)));
//            }
//        }
//        return imageList;
//    }

    public class Image {

        double leftx;
        double rightx;
        double upy;
        double downy;


        public Image(double lx, double rx, double uy, double dy) {
            this.leftx = lx;
            this.rightx = rx;
            this.upy = uy;
            this.downy = dy;
        }

        public double getDowny() {
            return downy;
        }

        public double getLeftx() {
            return leftx;
        }

        public double getRightx() {
            return rightx;
        }

        public double getUpy() {
            return upy;
        }

        public boolean isWithin(double xcoord, double ycoord) {
            return (xcoord > this.leftx && xcoord <= this.rightx && ycoord < this.upy && ycoord >= this.downy);
        }

        public boolean isWithinOrRight(double xcoord, double ycoord) {
            if (xcoord > this.leftx && xcoord <= this.rightx && ycoord < this.upy && ycoord >= this.downy) {
                return true;
            }
            if (xcoord > this.rightx && ycoord < this.upy && ycoord >= this.downy) {
                return true;
            }
            return false;
        }

        public boolean isWithinOrLeft(double xcoord, double ycoord) {
            if (xcoord > this.leftx && xcoord <= this.rightx && ycoord < this.upy && ycoord >= this.downy) {
                return true;
            }
            if (xcoord < this.leftx && ycoord < this.upy && ycoord >= this.downy) {
                return true;
            }
            return false;
        }

        public boolean isWithinOrUp(double xcoord, double ycoord) {
            if (xcoord > this.leftx && xcoord <= this.rightx && ycoord < this.upy && ycoord >= this.downy) {
                return true;
            }
            if (xcoord > this.leftx && xcoord <= this.rightx && ycoord > this.upy) {
                return true;
            }
            return false;
        }

        public boolean isWithinOrDown(double xcoord, double ycoord) {
            if (xcoord > this.leftx && xcoord <= this.rightx && ycoord < this.upy && ycoord >= this.downy) {
                return true;
            }
            if (xcoord > this.leftx && xcoord <= this.rightx && ycoord < this.downy) {
                return true;
            }
            return false;
        }
    }

    /**
     * Calculates the lonDPP of an image or query box
     *
     * @param lrlon Lower right longitudinal value of the image or query box
     * @param ullon Upper left longitudinal value of the image or query box
     * @param width Width of the query box or image
     * @return lonDPP
     */
    private double lonDPP(double lrlon, double ullon, double width) {
        return (lrlon - ullon) / width;
    }

    private double[] calcParams(int depth, int leftx, int rightx, int upy, int downy) {
        Image[][] input = imageLongLat(depth);
        double[] newParams = new double[4];
        if (input[leftx][upy].leftx >= MapServer.ROOT_ULLON) {
            newParams[0] = input[leftx][upy].leftx;
        } else {
            newParams[0] = MapServer.ROOT_ULLAT;
        }
        if (input[rightx][downy].rightx <= MapServer.ROOT_LRLON) {
            newParams[1] = input[rightx][downy].rightx;
        } else {
            newParams[1] = MapServer.ROOT_LRLON;
        }
        if (input[leftx][upy].upy <= MapServer.ROOT_ULLAT) {
            newParams[2] = input[leftx][upy].upy;
        } else {
            newParams[2] = MapServer.ROOT_ULLAT;
        }
        if (input[rightx][downy].downy >= MapServer.ROOT_LRLAT) {
            newParams[3] = input[rightx][downy].downy;
        } else {
            newParams[3] = MapServer.ROOT_LRLAT;
        }

        return newParams;
    }
}
