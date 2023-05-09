package com.gsc.apps.framework.lifecycle;


import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.framework.lifecycle.*;
import com.matrixone.apps.framework.lifecycle.ImageUrlConstants;
import com.matrixone.apps.framework.lifecycle.LifeCycleImageData;

import java.util.ArrayList;
import java.util.HashMap;

public class LifeCycleTablePresentation implements ImageUrlConstants {
    public LifeCycleTablePresentation() {
    }

    public ArrayList[] generateTableForPresentation(HashMap var1, String var2) {
        String var3 = (String)var1.get("PFmode");
        int var4 = (Integer)((Integer)var1.get("TOTALROWS"));
        int var5 = (Integer)((Integer)var1.get("TOTALCOLUMNS"));
        int var6 = (Integer)((Integer)var1.get("TOPSELFBRANCHES"));
        int var7 = (Integer)((Integer)var1.get("BOTTOMSELFBRANCHES"));
        ArrayList var8 = (ArrayList)var1.get("TOPBRANCHES");
        ArrayList var9 = (ArrayList)var1.get("BOTTOMBRANCHES");
        ArrayList var10 = (ArrayList)var1.get("STATEDETAILS");
        ArrayList[] var11 = new ArrayList[var4];

        int var12;
        for(var12 = 0; var12 < var4; ++var12) {
            var11[var12] = new ArrayList();
        }

        int var13;
        for(var12 = 0; var12 < var4; ++var12) {
            for(var13 = 0; var13 < var5; ++var13) {
                var11[var12].add(new ImageCell("images/utilSpacer.gif"));
            }
        }

        var12 = var8.size();

        int var16;
        for(var13 = 0; var13 < var8.size(); ++var13) {
            boolean var14 = false;
            InfoBranch var15 = (InfoBranch)var8.get(var13);
            var16 = var15.getStartColumnIndex();
            int var17 = var15.getEndColumnIndex();
            int var18 = var12;
            if (var6 > 0) {
                var18 = var12 - var6 + 1;
            }

            if (var16 == var17) {
                this.drawSelfBranch(var11, var10, var2, var16, var18, true, var3);
            } else {
                if (var16 > var17) {
                    var14 = true;
                }

                var11 = this.drawVerticalLine(var11, var16, var18, var13, true, true, var14);
                if (var14) {
                    var11 = this.drawHorizontalLine(var11, var10, var15, var2, var17, var16, var13, var14, var3);
                } else {
                    var11 = this.drawHorizontalLine(var11, var10, var15, var2, var16, var17, var13, var14, var3);
                }

                var11 = this.drawVerticalLine(var11, var17, var18, var13, false, true, var14);
            }
        }

        boolean var23 = false;
        if (var6 > 0) {
            var13 = var8.size() - var6 + 2;
        } else {
            var13 = var8.size() + 1;
        }

        String var24 = "";

        int var25;
        for(var25 = 0; var25 < var5 - 1; var25 += 3) {
            var16 = var25 / 3;
            InfoState var26 = (InfoState)var10.get(var16);
            String var19;
            InfoState var28;
            if (var26.getIsJumpFromPrevStateAllowed()) {
                var28 = (InfoState)var10.get(var16 - 1);
                if (var26.getIsSignReqdForJumpFromPrevState()) {
                    if (!var3.equals("true")) {
                        var24 = this.getSignatureURL(var28, var26, var2);
                        if (var28.getNextStateHasRoute()) {
                            var11[var13].set(var25, new URLImageCell("images/lifecycleSignatureWithRouteArrowW38.gif", var24));
                        } else if (var26.getIsSignHyperlinkedFromPrevState()) {
                            var11[var13].set(var25, new URLImageCell("images/lifecycleSignatureArrowW38.gif", var24));
                        } else {
                            var11[var13].set(var25, new ImageCell("images/lifecycleSignatureArrowW38.gif"));
                        }
                    } else {
                        var11[var13].set(var25, new ImageCell("images/lifecycleSignatureArrowW38.gif"));
                    }
                } else if (var28.getNextStateHasRoute()) {
                    var19 = this.getRouteURL(var28, var26, var2);
                    if (!var3.equals("true")) {
                        var11[var13].set(var25, new URLImageCell("images/lifecycleRouteArrowW38.gif", var19));
                    } else {
                        var11[var13].set(var25, new ImageCell("images/lifecycleRouteArrowW38.gif"));
                    }
                } else {
                    var11[var13].set(var25, new ImageCell("images/lifecycleHorizontalArrowW08.gif"));
                }
            } else {
                var11[var13].set(var25, new ImageCell("images/utilSpacer.gif"));
            }

            var11[var13].set(var25 + 1, new StateCell(var26));
            if (var26.getIsJumpToNextStateAllowed()) {
                if (var26.getNextStateHasRoute() && var26.getIsSignReqdForJumpToNextState()) {
                    var28 = (InfoState)var10.get(var16 + 1);
                    var19 = this.getRouteURL(var26, var28, var2);
                    if (!var3.equals("true")) {
                        var11[var13].set(var25 + 2, new URLImageCell("images/lifecycleRouteWithSignatureW22.gif", var19));
                    } else {
                        var11[var13].set(var25 + 2, new ImageCell("images/lifecycleRouteWithSignatureW22.gif"));
                    }
                } else {
                    var11[var13].set(var25 + 2, new ImageCell("images/lifecycleHorizontalLineW05.gif"));
                }
            } else {
                var11[var13].set(var25 + 2, new ImageCell("images/utilSpacer.gif"));
            }
        }

        var25 = var9.size();

        for(var16 = 0; var16 < var9.size(); ++var16) {
            InfoBranch var27 = (InfoBranch)var9.get(var16);
            boolean var30 = false;
            int var29 = var27.getStartColumnIndex();
            int var20 = var27.getEndColumnIndex();
            int var21 = var13 + 1;
            int var22 = var4 - var16 - 1;
            if (var29 == var20) {
                this.drawSelfBranch(var11, var10, var2, var29, var21, false, var3);
            } else {
                if (var29 > var20) {
                    var30 = true;
                }

                var11 = this.drawVerticalLine(var11, var29, var21, var22, true, false, var30);
                if (var30) {
                    var11 = this.drawHorizontalLine(var11, var10, var27, var2, var20, var29, var22, var30, var3);
                } else {
                    var11 = this.drawHorizontalLine(var11, var10, var27, var2, var29, var20, var22, var30, var3);
                }

                var11 = this.drawVerticalLine(var11, var20, var21, var22, false, false, var30);
            }
        }

        return var11;
    }

    public ArrayList[] drawSelfBranch(ArrayList[] var1, ArrayList var2, String var3, int var4, int var5, boolean var6) {
        return this.drawSelfBranch(var1, var2, var3, var4, var5, var6, "false");
    }

    public ArrayList[] drawSelfBranch(ArrayList[] var1, ArrayList var2, String var3, int var4, int var5, boolean var6, String var7) {
        ImageCell var8 = null;
        com.matrixone.apps.framework.lifecycle.LifeCycleImageData var9 = com.matrixone.apps.framework.lifecycle.LifeCycleImageData.getInstance();
        int var10 = var4 / 3;
        int var11 = var4 / 3;
        InfoState var12 = (InfoState)var2.get(var10);
        InfoState var13 = (InfoState)var2.get(var11);
        String var14 = this.getSignatureURL(var12, var13, var3);
        if (var6) {
            var8 = var9.getResultantImage((ImageCell)var1[var5 - 1].get(var4), "images/lifecycleTopSelfBranchW90.gif");
            if (!var7.equals("true")) {
                var1[var5 - 1].set(var4, new URLImageCell(var8.getImagePath(), var14));
            } else {
                var1[var5 - 1].set(var4, new ImageCell(var8.getImagePath()));
            }

            if ("images/lifecycleTopSelfBranchW90.gif".equals(var8.getImagePath())) {
                var1[var5].set(var4, new ImageCell("images/lifecycleSelfBranchDownArrowW90.gif"));
            } else {
                var8 = var9.getResultantImage((ImageCell)var1[var5].get(var4), var8.getImagePath());
                var1[var5].set(var4, var8);
            }
        } else {
            var8 = var9.getResultantImage((ImageCell)var1[var5 + 1].get(var4), "images/lifecycleBottomSelfBranchW90.gif");
            if (!var7.equals("true")) {
                var1[var5 + 1].set(var4, new URLImageCell(var8.getImagePath(), var14));
            } else {
                var1[var5 + 1].set(var4, new ImageCell(var8.getImagePath()));
            }

            if ("images/lifecycleBottomSelfBranchW90.gif".equals(var8.getImagePath())) {
                var1[var5].set(var4, new ImageCell("images/lifecycleSelfBranchUpArrowW90.gif"));
            } else {
                var8 = var9.getResultantImage((ImageCell)var1[var5].get(var4), var8.getImagePath());
                var1[var5].set(var4, var8);
            }
        }

        return var1;
    }

    public ArrayList[] drawVerticalLine(ArrayList[] var1, int var2, int var3, int var4, boolean var5, boolean var6, boolean var7) {
        com.matrixone.apps.framework.lifecycle.LifeCycleImageData var8 = com.matrixone.apps.framework.lifecycle.LifeCycleImageData.getInstance();
        int var9 = var3;
        boolean var10 = false;
        String var11;
        ImageCell var13;
        if (var6) {
            for(var11 = null; var3 >= var4; --var3) {
                ImageCell var12;
                if (var9 == var3) {
                    if (var5) {
                        var12 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleVerticalLineH10.gif");
                        var1[var3].set(var2, var12);
                        if (!"images/lifecycleVerticalLineH10.gif".equals(var12.getImagePath())) {
                            var10 = true;
                        }

                        if ("images/lifecycleDownArrowLineW20H10.gif".equals(var12.getImagePath())) {
                            if (var7) {
                                var1[var3].set(var2, new ImageCell("images/lifecycleDownRightArrowLineW90.gif"));
                            }

                            var10 = true;
                        }

                        var11 = ((ImageCell)var1[var3].get(var2)).getImagePath();
                    } else {
                        var12 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleDownArrowW20H10.gif");
                        var1[var3].set(var2, var12);
                        if (!"images/lifecycleDownArrowW20H10.gif".equals(var12.getImagePath())) {
                            var10 = true;
                        }

                        if ("images/lifecycleDownArrowRightLineW90.gif".equals(var12.getImagePath())) {
                            if (var7) {
                                var1[var3].set(var2, new ImageCell("images/lifecycleDownArrowLeftLineW90.gif"));
                            }

                            var10 = true;
                        }

                        var11 = ((ImageCell)var1[var3].get(var2)).getImagePath();
                    }
                } else if (var3 == var4) {
                    String var15;
                    if (var5) {
                        if (var7) {
                            var15 = ((ImageCell)var1[var3].get(var2)).getImagePath();
                            var13 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleTopRightCornerW90.gif");
                            var1[var3].set(var2, var13);
                            if (var10 && var15.equals("images/lifecycleVerticalLineW20H26.gif")) {
                                var13 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleTopRightCornerW90.gif");
                                var1[var3].set(var2, var13);
                            } else if (var15.equals("images/lifecycleParallelLeftVerticaLinesW90.gif") && var11.equals("images/lifecycleDownArrowRightLineW90.gif")) {
                                var1[var3].set(var2, new ImageCell("images/lifecycleTopRightParallelCornerCrossJunctionW90.gif"));
                            } else if (var15.equals("images/lifecycleParallelLeftVerticaLinesW90.gif") && var11.equals("images/lifecycleDownRightArrowLineW90.gif")) {
                                var1[var3].set(var2, new ImageCell("images/lifecycleTopRightParallelCornerJunctionW90.gif"));
                            }
                        } else {
                            var15 = ((ImageCell)var1[var3].get(var2)).getImagePath();
                            var13 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleTopLeftCornerW90.gif");
                            var1[var3].set(var2, var13);
                            if (var10 && var15.equals("images/lifecycleVerticalLineW20H26.gif")) {
                                var13 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleTopLeftCornerW90.gif");
                                var1[var3].set(var2, var13);
                            } else if (var15.equals("images/lifecycleParallelRightVerticalLinesW90.gif") && var11.equals("images/lifecycleDownArrowLeftLineW90.gif")) {
                                var1[var3].set(var2, new ImageCell("images/lifecycleTopLeftParallelCornerCrossJunctionW90.gif"));
                            } else if (var15.equals("images/lifecycleParallelRightVerticalLinesW90.gif") && var11.equals("images/lifecycleDownArrowLineW20H10.gif")) {
                                var1[var3].set(var2, new ImageCell("images/lifecycleTopLeftParallelCornerJunctionW90.gif"));
                            }
                        }
                    } else if (var7) {
                        var15 = ((ImageCell)var1[var3].get(var2)).getImagePath();
                        var13 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleTopLeftCornerW90.gif");
                        var1[var3].set(var2, var13);
                        if (var10 && var15.equals("images/lifecycleVerticalLineW20H26.gif")) {
                            var13 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleTopLeftCornerW90.gif");
                            var1[var3].set(var2, var13);
                        } else if (var15.equals("images/lifecycleParallelRightVerticalLinesW90.gif") && var11.equals("images/lifecycleDownArrowLineW20H10.gif")) {
                            var1[var3].set(var2, new ImageCell("images/lifecycleTopLeftParallelCornerCrossJunctionW90.gif"));
                        } else if (var15.equals("images/lifecycleParallelRightVerticalLinesW90.gif") && var11.equals("images/lifecycleDownArrowLeftLineW90.gif")) {
                            var1[var3].set(var2, new ImageCell("images/lifecycleTopLeftParallelCornerJunctionW90.gif"));
                        }
                    } else {
                        var15 = ((ImageCell)var1[var3].get(var2)).getImagePath();
                        var13 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleTopRightCornerW90.gif");
                        var1[var3].set(var2, var13);
                        if (var10 && var15.equals("images/lifecycleVerticalLineW20H26.gif")) {
                            var13 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleTopRightCornerW90.gif");
                            var1[var3].set(var2, var13);
                        } else if (var15.equals("images/lifecycleParallelRightVerticalLinesW90.gif") && var11.equals("images/lifecycleDownRightArrowLineW90.gif")) {
                            var1[var3].set(var2, new ImageCell("images/lifecycleTopRightParallelCornerCrossJunctionW90.gif"));
                        } else if (var15.equals("images/lifecycleParallelRightVerticalLinesW90.gif") && var11.equals("images/lifecycleDownArrowRightLineW90.gif")) {
                            var1[var3].set(var2, new ImageCell("images/lifecycleTopLeftParallelCornerJunctionW90.gif"));
                        }
                    }
                } else if (var10) {
                    if (var7) {
                        if (var5) {
                            var12 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleParallelLeftVerticaLinesW90.gif");
                            var1[var3].set(var2, var12);
                        } else {
                            var12 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleParallelRightVerticalLinesW90.gif");
                            var1[var3].set(var2, var12);
                        }
                    } else if (var5) {
                        var12 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleParallelRightVerticalLinesW90.gif");
                        var1[var3].set(var2, var12);
                    } else {
                        var12 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleParallelLeftVerticaLinesW90.gif");
                        var1[var3].set(var2, var12);
                    }
                } else {
                    var1[var3].set(var2, new ImageCell("images/lifecycleVerticalLineW20H26.gif"));
                }
            }
        } else {
            var11 = null;

            for(boolean var16 = false; var3 <= var4; ++var3) {
                ImageCell var14;
                String var17;
                if (var9 == var3) {
                    if (var5) {
                        var13 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleVerticalLineH10.gif");
                        var1[var3].set(var2, var13);
                        if (!"images/lifecycleVerticalLineH10.gif".equals(var13.getImagePath())) {
                            var10 = true;
                        }

                        if ("images/lifecycleUpArrowRightLineW90.gif".equals(var13.getImagePath())) {
                            if (var7) {
                                var1[var3].set(var2, new ImageCell("images/lifecycleUpRightArrowLineW90.gif"));
                            }

                            var10 = true;
                        }

                        var11 = ((ImageCell)var1[var3].get(var2)).getImagePath();
                    } else {
                        var17 = ((ImageCell)var1[var3].get(var2)).getImagePath();
                        var14 = null;
                        if ("images/lifecycleUpArrowLineW20H10.gif".equals(var17)) {
                            var14 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleUpArrowLineW20H10.gif");
                            var16 = true;
                        } else if (!"images/lifecycleUpArrowLineW20H10.gif".equals(var17)) {
                            var14 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleUpArrowW20H10.gif");
                        } else {
                            var14 = new ImageCell(var17);
                        }

                        var1[var3].set(var2, var14);
                        if (!"images/lifecycleUpArrowW20H10.gif".equals(var14.getImagePath())) {
                            var10 = true;
                        }

                        if ("images/lifecycleUpArrowLineW20H10.gif".equals(var14.getImagePath())) {
                            if (var7 && !var16) {
                                var1[var3].set(var2, new ImageCell("images/lifecycleUpLeftArrowLineW90.gif"));
                            }

                            var10 = true;
                        }

                        var11 = ((ImageCell)var1[var3].get(var2)).getImagePath();
                    }
                } else if (var3 == var4) {
                    if (var5) {
                        if (var7) {
                            var17 = ((ImageCell)var1[var3].get(var2)).getImagePath();
                            var14 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleBottomRightCornerW90.gif");
                            var1[var3].set(var2, var14);
                            if (var10 && var17.equals("images/lifecycleVerticalLineW20H26.gif")) {
                                var14 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleBottomRightCornerW90.gif");
                                var1[var3].set(var2, var14);
                            } else if (var17.equals("images/lifecycleParallelLeftVerticaLinesW90.gif") && var11.equals("images/lifecycleUpArrowRightLineW90.gif")) {
                                var1[var3].set(var2, new ImageCell("images/lifecycleBottomRightParallelCornerCrossJunctionW90.gif"));
                            } else if (var17.equals("images/lifecycleParallelLeftVerticaLinesW90.gif") && var11.equals("images/lifecycleUpRightArrowLineW90.gif")) {
                                var1[var3].set(var2, new ImageCell("images/lifecycleBottomRightParallelCornerJunctionW90.gif"));
                            }
                        } else {
                            var17 = ((ImageCell)var1[var3].get(var2)).getImagePath();
                            var14 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleBottomLeftCornerW90.gif");
                            var1[var3].set(var2, var14);
                            if (var10 && var17.equals("images/lifecycleVerticalLineW20H26.gif")) {
                                var14 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleBottomLeftCornerW90.gif");
                                var1[var3].set(var2, var14);
                            } else if (var17.equals("images/lifecycleParallelRightVerticalLinesW90.gif") && var11.equals("images/lifecycleUpLeftArrowLineW90.gif")) {
                                var1[var3].set(var2, new ImageCell("images/lifecycleBottomLeftParallelCornerCrossJunctionW90.gif"));
                            } else if (var17.equals("images/lifecycleParallelRightVerticalLinesW90.gif") && var11.equals("images/lifecycleUpArrowRightLineW90.gif")) {
                                var1[var3].set(var2, new ImageCell("images/lifecycleBottomLeftParallelCornerJunctionW90.gif"));
                            }
                        }
                    } else if (var7) {
                        var17 = ((ImageCell)var1[var3].get(var2)).getImagePath();
                        var14 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleBottomLeftCornerW90.gif");
                        var1[var3].set(var2, var14);
                        if (var10 && var17.equals("images/lifecycleVerticalLineW20H26.gif")) {
                            var14 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleBottomLeftCornerW90.gif");
                            var1[var3].set(var2, var14);
                        } else if (var17.equals("images/lifecycleParallelRightVerticalLinesW90.gif") && var11.equals("images/lifecycleUpArrowRightLineW90.gif")) {
                            var1[var3].set(var2, new ImageCell("images/lifecycleBottomLeftParallelCornerCrossJunctionW90.gif"));
                        } else if (var17.equals("images/lifecycleParallelRightVerticalLinesW90.gif") && var11.equals("images/lifecycleUpLeftArrowLineW90.gif")) {
                            var1[var3].set(var2, new ImageCell("images/lifecycleBottomLeftParallelCornerJunctionW90.gif"));
                        } else if (var17.equals("images/lifecycleParallelLeftVerticaLinesW90.gif") && var11.equals("images/lifecycleUpArrowLineW20H10.gif")) {
                            var1[var3].set(var2, new ImageCell("images/lifecycleBottomLeftParallelCrossCornerJunction1W90.gif"));
                        }
                    } else {
                        var17 = ((ImageCell)var1[var3].get(var2)).getImagePath();
                        var14 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleBottomRightCornerW90.gif");
                        var1[var3].set(var2, var14);
                        if (var10 && var17.equals("images/lifecycleVerticalLineW20H26.gif")) {
                            var14 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleBottomRightCornerW90.gif");
                            var1[var3].set(var2, var14);
                        } else if (var17.equals("images/lifecycleParallelLeftVerticaLinesW90.gif") && var11.equals("images/lifecycleUpRightArrowLineW90.gif")) {
                            var1[var3].set(var2, new ImageCell("images/lifecycleBottomRightParallelCornerCrossJunctionW90.gif"));
                        } else if (var17.equals("images/lifecycleParallelLeftVerticaLinesW90.gif") && var11.equals("images/lifecycleUpArrowLineW20H10.gif")) {
                            var1[var3].set(var2, new ImageCell("images/lifecycleTopRightParallelCornerJunctionW90.gif"));
                        }
                    }
                } else if (var10) {
                    if (var7) {
                        if (var5) {
                            var13 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleParallelLeftVerticaLinesW90.gif");
                            var1[var3].set(var2, var13);
                        } else {
                            var13 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleParallelRightVerticalLinesW90.gif");
                            var1[var3].set(var2, var13);
                        }
                    } else if (var5) {
                        var13 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleParallelRightVerticalLinesW90.gif");
                        var1[var3].set(var2, var13);
                    } else {
                        var13 = var8.getResultantImage((ImageCell)var1[var3].get(var2), "images/lifecycleParallelLeftVerticaLinesW90.gif");
                        var1[var3].set(var2, var13);
                    }
                } else {
                    var1[var3].set(var2, new ImageCell("images/lifecycleVerticalLineW20H26.gif"));
                }
            }
        }

        return var1;
    }

    public String getSignatureURL(InfoState var1, InfoState var2, String var3) {
        StringBuffer var4 = new StringBuffer(256);
        var4.append("emxLifecycleSignatures.jsp?objectId=");
        var4.append(var3);
        var4.append("&fromState=");
        var4.append(var1.getName());
        var4.append("&toState=");
        var4.append(var2.getName());
        var4.append("&isInCurrentState=");
        var4.append(var1.getIsCurrent());
        String var5 = FrameworkUtil.encodeURLParamValues(var4.toString());
        var5 = "javascript:showDetailsPopup('" + var5 + "');";
        return var5;
    }

    public String getRouteURL(InfoState var1, InfoState var2, String var3) {
        StringBuffer var4 = new StringBuffer(256);
        var4.append("emxLifecycleRoutes.jsp?objectId=");
        var4.append(var3);
        var4.append("&fromState=");
        var4.append(var1.getName());
        var4.append("&toState=");
        var4.append(var2.getName());
        var4.append("&isInCurrentState=");
        var4.append(var1.getIsCurrent());
        String var5 = FrameworkUtil.encodeURLParamValues(var4.toString());
        var5 = "javascript:showDetailsPopup('" + var5 + "');";
        return var5;
    }

    public ArrayList[] drawHorizontalLine(ArrayList[] var1, ArrayList var2, InfoBranch var3, String var4, int var5, int var6, int var7, boolean var8) {
        return this.drawHorizontalLine(var1, var2, var3, var4, var5, var6, var7, var8, "false");
    }

    public ArrayList[] drawHorizontalLine(ArrayList[] var1, ArrayList var2, InfoBranch var3, String var4, int var5, int var6, int var7, boolean var8, String var9) {
        com.matrixone.apps.framework.lifecycle.LifeCycleImageData var10 = LifeCycleImageData.getInstance();
        int var11 = var5 / 3;
        int var12 = var6 / 3;
        InfoState var13 = (InfoState)var2.get(var11);
        InfoState var14 = (InfoState)var2.get(var12);
        String var15 = null;
        String var16 = null;
        if (var8) {
            var15 = this.getSignatureURL(var14, var13, var4);
            var16 = this.getRouteURL(var14, var13, var4);
        } else {
            var15 = this.getSignatureURL(var13, var14, var4);
            var16 = this.getRouteURL(var13, var14, var4);
        }

        int var17 = var6 - var5 - 1;
        boolean var18 = false;
        int var22 = var6 - 2;
        int var19 = (var6 - var5) / 3;

        for(int var20 = var5 + 1; var19 > 0; --var19) {
            if (var20 != var22) {
                var1[var7].set(var20++, new ImageCell("images/lifecycleHorizontalLineW05.gif"));
                var1[var7].set(var20++, new ImageCell("images/lifecycleHorizontalLineW05.gif"));
            } else if (var3.hasRoute()) {
                if (!var9.equals("true")) {
                    var1[var7].set(var20++, new URLImageCell("images/lifecycleRouteLineW22.gif", var16));
                    if (var3.hasSignatureRequirement()) {
                        var1[var7].set(var20++, new URLImageCell("images/lifecycleSignatureLineW38.gif", var15));
                    } else {
                        var1[var7].set(var20++, new ImageCell("images/lifecycleHorizontalLineW05.gif"));
                    }
                } else {
                    var1[var7].set(var20++, new ImageCell("images/lifecycleRouteLineW22.gif"));
                    if (var3.hasSignatureRequirement()) {
                        var1[var7].set(var20++, new ImageCell("images/lifecycleSignatureLineW38.gif"));
                    } else {
                        var1[var7].set(var20++, new ImageCell("images/lifecycleHorizontalLineW05.gif"));
                    }
                }
            } else if (!var3.hasSignatureRequirement()) {
                var1[var7].set(var20++, new ImageCell("images/lifecycleHorizontalLineW05.gif"));
                var1[var7].set(var20++, new ImageCell("images/lifecycleHorizontalLineW05.gif"));
            } else {
                if (!var9.equals("true")) {
                    var1[var7].set(var20++, new URLImageCell("images/lifecycleSignatureLineW22.gif", var15));
                } else {
                    var1[var7].set(var20++, new ImageCell("images/lifecycleSignatureLineW22.gif"));
                }

                var1[var7].set(var20++, new ImageCell("images/lifecycleHorizontalLineW05.gif"));
            }

            if (var20 != var22) {
                if (var20 < var6) {
                    ImageCell var21 = var10.getResultantImage((ImageCell)var1[var7].get(var20), "images/lifecycleHorizontalLineW90.gif");
                    var1[var7].set(var20++, var21);
                }
            } else if (!var9.equals("true")) {
                var1[var7].set(var20++, new URLImageCell("images/lifecycleSignatureLineW90.gif", var15));
            } else {
                var1[var7].set(var20++, new ImageCell("images/lifecycleSignatureLineW90.gif"));
            }
        }

        return var1;
    }
}
