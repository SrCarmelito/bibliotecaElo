import { useCallback, useEffect, useRef, useState } from "react";

export const useLoading = () => {
  const mounted = useRef(false);
  const [loading, setLoading] = useState(false);
  const [pendingPromises, setPendingPromises] = useState(0);
  const awaiting = useRef<((didLoad: boolean) => void)[]>([]);

  useEffect(() => {
    mounted.current = true;

    return () => {
      mounted.current = false;
    };
  }, []);

  useEffect(() => {
    if (mounted.current) {
      const isLoading = pendingPromises > 0;
      setLoading(isLoading);
      if (!isLoading) {
        awaiting.current.forEach((resolve) => resolve(true));
        awaiting.current = [];
      }
    }
  }, [pendingPromises]);

  const fetch = useCallback(
    async <T = any,>(promise: Promise<any>): Promise<T> => {
      setPendingPromises((promises) => promises + 1);

      return promise.finally(() => {
        if (mounted.current) {
          setPendingPromises((promises) => promises - 1);
        }
      });
    },
    [setPendingPromises]
  );

  const awaitLoading = useCallback(
    (shouldLoad: boolean = false) =>
      new Promise((resolve) => {
        if (!loading && !shouldLoad) {
          return resolve(false);
        }
        awaiting.current.push(resolve);
      }),
    [loading]
  );

  return [loading, fetch, awaitLoading] as const;
};
