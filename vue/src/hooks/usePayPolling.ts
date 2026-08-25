import { onUnmounted } from "vue";
import { ElMessage } from "element-plus";
import { queryPayApi } from "@/api/pay/index.ts";

export default function usePayPolling() {
    let timer: ReturnType<typeof setInterval> | null = null;
    let count = 0;

    //开始轮询支付状态,命中已支付即停止并回调。
    //activeQuery=true 会主动调用支付宝查询交易状态,异步通知(穿透)失效也能兜底。
    const start = (
        bizType: string,
        orderId: string,
        onSuccess: () => void,
        intervalMs = 2000,
        maxCount = 30
    ) => {
        stop();
        count = 0;
        const tick = async () => {
            count++;
            try {
                let res = await queryPayApi({ bizType, orderId, activeQuery: true });
                if (res && res.code == 200 && res.data && res.data.payStatus == '1') {
                    stop();
                    onSuccess();
                    ElMessage.success("支付成功");
                    return;
                }
            } catch (e) {
                //网络波动忽略,继续轮询
            }
            if (count >= maxCount) {
                stop();
                ElMessage.warning("查询支付状态超时,请刷新页面查看最新状态");
            }
        };
        //立即先查一次,再按间隔轮询
        tick();
        timer = setInterval(tick, intervalMs);
    };

    //停止轮询
    const stop = () => {
        if (timer) {
            clearInterval(timer);
            timer = null;
        }
    };

    onUnmounted(() => {
        stop();
    });

    return { start, stop };
}
