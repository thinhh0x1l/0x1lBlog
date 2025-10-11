import dayjs from "dayjs";

export const formatDate = (value, format = 'YYYY-MM-DD HH:mm:ss') => {
    if(!value)
        return'';
    return dayjs(value).format(format);
}